package fb.crawler.post;

import datastore.FBPostCommentRepository;
import datastore.FBPostLikeRepository;
import datastore.FBPostRepository;
import datastore.FBTokenRepository;
import datastore.dao.PostCommentDao;
import datastore.dao.PostDao;
import datastore.dao.PostLikeDao;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Slf4j
public class FetchPosts {
    private static String BASE_URL = "https://graph.facebook.com/";
    private static final String POSTS_LABEL = "posts";
    private static final String ACCESS_TOKEN_LABEL = "access_token";
    private Provider<FetchFBPostCommand> fetchPostCommandProvider;
    private Provider<FetchFBPostLikeCommand> fetchFBPostLikeCommandProvider;
    private Provider<FetchFBPostCommentCommand> fetchFBPostCommentCommandProvider;
    private FBPostRepository fbPostRepository;
    private FBTokenRepository fbTokenRepository;
    private FBPostLikeRepository fbPostLikeRepository;
    private FBPostCommentRepository fbPostCommentRepository;
    private final ExecutorService executorService;

    @Inject
    public FetchPosts(Provider<FetchFBPostCommand> fetchPostCommandProvider, Provider<FetchFBPostLikeCommand> fetchFBPostLikeCommandProvider, Provider<FetchFBPostCommentCommand> fetchFBPostCommentCommandProvider, FBPostRepository fbPostRepository, FBTokenRepository fbTokenRepository, FBPostLikeRepository fbPostLikeRepository, FBPostCommentRepository fbPostCommentRepository, ExecutorService executorService) {
        this.fetchPostCommandProvider = fetchPostCommandProvider;
        this.fetchFBPostLikeCommandProvider = fetchFBPostLikeCommandProvider;
        this.fetchFBPostCommentCommandProvider = fetchFBPostCommentCommandProvider;
        this.fbPostRepository = fbPostRepository;
        this.fbTokenRepository = fbTokenRepository;
        this.fbPostLikeRepository = fbPostLikeRepository;
        this.fbPostCommentRepository = fbPostCommentRepository;
        this.executorService = executorService;
    }

    public boolean run(String fbPageId){
        URI uri = getUriToFetchPostForFBpage(fbPageId);
        Posts posts = fetchPostCommandProvider.get().withUri(uri).execute();
        Optional<URI> nextPageUri = getNextPageUri(posts);
        List<PostDao> postDaoList=  convertToPostDao(posts.getData());
        fetchUserLikesFromPosts(posts);
        fetchCommentsFromPosts(posts);
        int numOfPostInserted = fbPostRepository.perist(postDaoList);
        log.debug("Post persisted " + numOfPostInserted);
        while (nextPageUri.isPresent()){
            posts = fetchPostCommandProvider.get().withUri(nextPageUri.get()).execute();
            nextPageUri = getNextPageUri(posts);
            fetchUserLikesFromPosts(posts);
            fetchCommentsFromPosts(posts);
            postDaoList=  convertToPostDao(posts.getData());
            numOfPostInserted = fbPostRepository.perist(postDaoList);
            log.debug("Post persisted " + numOfPostInserted + " total posts "+ fbPostRepository.count());
        }
        return true;
    }

    private List<PostDao> convertToPostDao(List<Posts.Post> postList){
        return postList.stream().map(p -> new PostDao(p.getId(), p.getMessage())).collect(Collectors.toList());
    }

    private void fetchUserLikesFromPosts(Posts posts) {
        log.debug("Likes.........");
        executorService.submit(()->{
            String postId = posts.getData().get(0).getId();
            posts.getData().stream().map(Posts.Post::getLikes).forEach(p -> getUserLikesFromLikes(p, postId));
        });

    }

    private void getUserLikesFromLikes(Posts.Likes likes, String postId) {
        List<Posts.UserInfo> userInfos = likes.getData();
        List<PostLikeDao> postLikeDaos = convertToUserLikeDao(userInfos, postId);
        int persisted = fbPostLikeRepository.persist(postLikeDaos);
        log.debug("Post Like persisted "+ persisted);
        Optional<URI> nextPageUri = getNextPageUri(likes);
        while (nextPageUri.isPresent()){
            likes = fetchFBPostLikeCommandProvider.get().withUri(nextPageUri.get()).execute();
            nextPageUri = getNextPageUri(likes);
            postLikeDaos = convertToUserLikeDao(likes.getData(), postId);
            persisted = fbPostLikeRepository.persist(postLikeDaos);
            log.debug("Post Like persisted "+ persisted);
        }
    }

    private Optional<URI> getNextPageUri(Posts.Likes likes) {
        Optional<Posts.Paging> nextUri = Optional.ofNullable(likes.getPaging());
        return nextUri.flatMap(s -> Optional.ofNullable(UriBuilder.fromUri(s.getNext()).build()));
    }

    private List<PostLikeDao> convertToUserLikeDao(List<Posts.UserInfo> userInfos, String postId) {
        return userInfos.stream().map(l -> new PostLikeDao(l.getId(), l.getName(), postId)).collect(Collectors.toList());
    }

    private void fetchCommentsFromPosts(Posts posts) {
        executorService.submit(()->{
            String postId = posts.getData().get(0).getId();
            posts.getData().stream().map(Posts.Post::getComments).forEach(cs -> fetchComments(cs, postId));
        });

    }

    private void fetchComments(Posts.Comments cs, String postId) {
        List<Posts.Comment> comments = cs.getData();
        List<PostCommentDao> commentDaos = convertToCommentDao(comments, postId);
        int persisted =fbPostCommentRepository.persist(commentDaos);
        log.debug("Post Comments persisted "+ persisted);
        Optional<URI> nextPageUri = getNextPageUri(cs);
        while (nextPageUri.isPresent()){
            cs = fetchFBPostCommentCommandProvider.get().withUri(nextPageUri.get()).execute();
            nextPageUri = getNextPageUri(cs);
            commentDaos = convertToCommentDao(cs.getData(), postId);
            persisted = fbPostCommentRepository.persist(commentDaos);
            log.debug("Post Comments persisted "+ persisted);
        }

    }

    private Optional<URI> getNextPageUri(Posts.Comments cs) {
        Optional<Posts.Paging> nextUri = Optional.ofNullable(cs.getPaging());
        return nextUri.flatMap(s -> Optional.ofNullable(UriBuilder.fromUri(s.getNext()).build()));
    }

    private List<PostCommentDao> convertToCommentDao(List<Posts.Comment> comments, String postId) {
        return comments.stream().map(c -> new PostCommentDao(c.getId(), c.getFrom().getId(), c.getFrom().getName(), c.getMessage(),postId)).collect(Collectors.toList());
    }

    private Optional<URI> getNextPageUri(Posts posts) {
        Optional<Posts.Paging> nextUri = Optional.ofNullable(posts.getPaging());
        return nextUri.flatMap(s -> Optional.ofNullable(UriBuilder.fromUri(s.getNext()).build()));
    }

    private URI getUriToFetchPostForFBpage(String fbPageId) {
        String accessToken = fbTokenRepository.getRandomToken();
        return UriBuilder.fromUri(BASE_URL).path(fbPageId + "/" + POSTS_LABEL)
                .queryParam(ACCESS_TOKEN_LABEL, accessToken).build();
    }
}
