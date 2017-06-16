package fb.crawler.post;

import datastore.FBPostRepository;
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

    @Inject
    public FetchPosts(Provider<FetchPostCommand> fetchPostCommandProvider, FBPostRepository fbPostRepository) {
        this.fetchPostCommandProvider = fetchPostCommandProvider;
        this.fbPostRepository = fbPostRepository;
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
        String accessToken = "EAACEdEose0cBAPK70ggRRNJTIWQ4VfmvQZCsePnFGAJq7JemIMIzSlqq8Frs6GjUqAkswbmf2ud046MVCRZCBEUQyAoHOkzm1l1nRPliIKtgJnoPmrqBtYeRO11O7bQ2iLPpJ4UEWwicpVau81ZCUliQAsVYLZBnNdz46YNNOvkr1S8awWyJPmBxDMmTCUoZD";
        URI uri = UriBuilder.fromUri(BASE_URL).path(fbPageId + "/" + POSTS_LABEL)
                .queryParam(ACCESS_TOKEN_LABEL, accessToken).build();
        return uri;
    }
}
