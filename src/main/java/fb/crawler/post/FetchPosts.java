package fb.crawler.post;

import datastore.FBPostRepository;
import datastore.FBTokenReposistory;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Optional;

@Slf4j
public class FetchPosts {
    private static String BASE_URL = "https://graph.facebook.com/";
    private static final String POSTS_LABEL = "posts";
    private static final String ACCESS_TOKEN_LABEL = "access_token";
    private Provider<FetchPostCommand> fetchPostCommandProvider;
    private FBPostRepository fbPostRepository;
    private FBTokenReposistory fbTokenReposistory;

    @Inject
    public FetchPosts(Provider<FetchPostCommand> fetchPostCommandProvider, FBPostRepository fbPostRepository, FBTokenReposistory fbTokenReposistory) {
        this.fetchPostCommandProvider = fetchPostCommandProvider;
        this.fbPostRepository = fbPostRepository;
        this.fbTokenReposistory = fbTokenReposistory;
    }

    public boolean execute(String fbPageId){
        URI uri = getUriToFetchPostForFBpage(fbPageId);
        Posts posts = fetchPostCommandProvider.get().withUri(uri).execute();
        Optional<URI> nextPageUri = getNextPageUri(posts);
        int numOfPostInserted = fbPostRepository.perist(posts);
        log.info("Post persisted " + numOfPostInserted);
        while (nextPageUri.isPresent()){
            posts = fetchPostCommandProvider.get().withUri(nextPageUri.get()).execute();
            nextPageUri = getNextPageUri(posts);
            numOfPostInserted = fbPostRepository.perist(posts);
            log.info("Post persisted " + numOfPostInserted + " total posts "+ fbPostRepository.count());
        }
        return true;
    }

    private Optional<URI> getNextPageUri(Posts posts) {
        Optional<Posts.Paging> nextUri = Optional.ofNullable(posts.getPaging());
        return nextUri.flatMap(s -> Optional.ofNullable(UriBuilder.fromUri(s.getNext()).build()));
    }

    private URI getUriToFetchPostForFBpage(String fbPageId) {
        String accessToken = fbTokenReposistory.getRandomToken();
        return UriBuilder.fromUri(BASE_URL).path(fbPageId + "/" + POSTS_LABEL)
                .queryParam(ACCESS_TOKEN_LABEL, accessToken).build();
    }
}
