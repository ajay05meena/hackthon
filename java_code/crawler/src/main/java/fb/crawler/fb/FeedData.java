package fb.crawler.fb;

import datastore.dao.PostCommentDao;
import datastore.dao.PostDao;
import datastore.dao.PostLikeDao;
import fb.crawler.fb.command.FetchFBPostCommand;
import fb.crawler.fb.command.FetchFBPostCommentCommand;
import fb.crawler.fb.command.FetchFBPostLikeCommand;
import fb.crawler.fb.command.GetFbPageDetailCommand;
import fb.crawler.fb.model.FBPageDetail;
import fb.crawler.fb.model.Posts;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class FeedData {
    private Provider<FetchFBPostCommand> fetchPostCommandProvider;
    private Provider<FetchFBPostLikeCommand> fetchFBPostLikeCommandProvider;
    private Provider<FetchFBPostCommentCommand> fetchFBPostCommentCommandProvider;
    private final Provider<GetFbPageDetailCommand> getFbPageDetailCommandProvider;
    private final DataStoreService dataStoreService;



    @Inject
    public FeedData(Provider<FetchFBPostCommand> fetchPostCommandProvider, Provider<FetchFBPostLikeCommand> fetchFBPostLikeCommandProvider, Provider<FetchFBPostCommentCommand> fetchFBPostCommentCommandProvider, Provider<GetFbPageDetailCommand> getFbPageDetailCommandProvider, DataStoreService dataStoreService) {
        this.fetchPostCommandProvider = fetchPostCommandProvider;
        this.fetchFBPostLikeCommandProvider = fetchFBPostLikeCommandProvider;
        this.fetchFBPostCommentCommandProvider = fetchFBPostCommentCommandProvider;
        this.getFbPageDetailCommandProvider = getFbPageDetailCommandProvider;
        this.dataStoreService = dataStoreService;
    }

    public FBPageDetail pageDetail(String fbPageId){
        return getFbPageDetailCommandProvider.get().withPageId(fbPageId).execute();
    }


    public List<PostDao> getFeed(String fbPageId){
        List<PostDao> result = new ArrayList<>();
        URI uri = getUriToFetchPostForFBpage(fbPageId);
        Posts posts = fetchPostCommandProvider.get().withUri(uri).execute();
        Optional<URI> nextPageUri = getNextPostPageUri(posts);
        result.addAll(insertData(posts));
        while (nextPageUri.isPresent()){
            posts = fetchPostCommandProvider.get().withUri(nextPageUri.get()).execute();
            nextPageUri = getNextPostPageUri(posts);
            result.addAll(insertData(posts));
        }
        return result;
    }

    private List<PostDao> insertData(Posts posts) {
        List<PostDao> postDaoList =  Adaptor.convertToPostDao(posts.getData());
        //fetchUserLikesFromPosts(posts);
        //fetchCommentsFromPosts(posts);
        int numOfPostInserted = dataStoreService.persistPosts(postDaoList);
        log.debug("Post persisted " + numOfPostInserted);
        return postDaoList;
    }



    private void fetchUserLikesFromPosts(Posts posts) {
        log.debug("Likes.........");
        String postId = posts.getData().get(0).getId();
        posts.getData().parallelStream().map(Posts.Post::getLikes).forEach(p -> getUserLikesFromLikes(null, postId));


    }

    private void getUserLikesFromLikes(Posts.Likes likes, String postId) {
        likes = fetchFBPostLikeCommandProvider.get().withUri(getUriToFetchPostLikesForFBpage(postId)).execute();
        Optional<URI> nextPageUri = processUserLikes(likes, postId);
        while (nextPageUri.isPresent()){
            likes = fetchFBPostLikeCommandProvider.get().withUri(nextPageUri.get()).execute();
            processUserLikes(likes, postId);
        }
    }

    private Optional<URI> processUserLikes(Posts.Likes likes, String postId) {
        List<Posts.UserInfo> userInfoList = likes.getData();
        List<PostLikeDao> postLikeDaoList = Adaptor.convertToUserLikeDao(userInfoList, postId);
        int persisted = dataStoreService.persistPostLikes(postLikeDaoList);
        log.debug("Post Like persisted "+ persisted);
        return getNextPostPageUri(likes);
    }

    private Optional<URI> getNextPostPageUri(Posts.Likes likes) {
        Optional<Posts.Paging> nextUri = Optional.ofNullable(likes.getPaging());
        return nextUri.flatMap(s -> Optional.ofNullable(UriBuilder.fromUri(s.getNext()).build()));
    }



    private void fetchCommentsFromPosts(Posts posts) {
        String postId = posts.getData().get(0).getId();
        posts.getData().parallelStream().map(Posts.Post::getComments).forEach(cs -> fetchComments( postId));
    }

    private List<PostCommentDao> fetchComments(String postId) {
        List<PostCommentDao> res = new ArrayList<>();
        Posts.CommentsWrapper cs = fetchFBPostCommentCommandProvider.get().withUri(getUriToFetchPostCommentsForFBpage(postId)).execute();
        Optional<URI> nextPageUri = getNextPostPageUri(cs.getComments());
        res.addAll(processComments(cs, postId));
        while (nextPageUri.isPresent()){
            cs = fetchFBPostCommentCommandProvider.get().withUri(nextPageUri.get()).execute();
            nextPageUri = getNextPostPageUri(cs.getComments());
            res.addAll(processComments(cs, postId));
        }
        return res;
    }

    private List<PostCommentDao> processComments(Posts.CommentsWrapper cs, String postId) {
        if(cs==null|| cs.getComments()==null){
            return new ArrayList<>();
        }
        List<Posts.Comment> comments = cs.getComments().getData();
        List<PostCommentDao> postCommentDaoList = Adaptor.convertToCommentDao(comments, postId);
        int persisted = dataStoreService.persistPostComments(postCommentDaoList);
        log.debug("Post Comments persisted "+ persisted);
        return postCommentDaoList;
    }

    private Optional<URI> getNextPostPageUri(Posts.Comments cs) {
        if(cs==null || cs.getPaging()==null||cs.getPaging().getNext()==null){
            return Optional.empty();
        }
        Optional<Posts.Paging> nextUri = Optional.ofNullable(cs.getPaging());
        if(nextUri.isPresent()){
            return nextUri.flatMap(s -> Optional.ofNullable(UriBuilder.fromUri(s.getNext()).build()));
        }else{
            return Optional.empty();
        }

    }



    private Optional<URI> getNextPostPageUri(Posts posts) {
        Optional<Posts.Paging> nextUri = Optional.ofNullable(posts.getPaging());
        return nextUri.flatMap(s -> Optional.ofNullable(UriBuilder.fromUri(s.getNext()).build()));
    }

    private URI getUriToFetchPostForFBpage(String fbPageId) {
        return UriBuilder.fromUri(Constants.BASE_URL).path(fbPageId + "/" + Constants.POSTS_LABEL).build();
    }

    private URI getUriToFetchPostLikesForFBpage(String postId) {
        return UriBuilder.fromUri(Constants.BASE_URL).path(postId + "/" + Constants.POSTS_LIKES).build();
    }


    private URI getUriToFetchPostCommentsForFBpage(String postId) {
        return UriBuilder.fromUri(Constants.BASE_URL).path(postId).queryParam( "fields" ,Constants.POSTS_COMMENTS).build();
    }

    public List<PostCommentDao> comments(String postId) {
        return fetchComments(postId);
    }
}
