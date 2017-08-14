package fb.crawler;

import datastore.FBPageRepository;
import fb.crawler.post.FetchPosts;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.ExecutorService;


@Slf4j
public class FBCrawler {
    private final Provider<FetchPosts> fetchPostsProvider;
    private final FBPageRepository fbPageRepository;
    private final ExecutorService executorService;

    @Inject
    public FBCrawler(Provider<FetchPosts> fetchPostsProvider, FBPageRepository fbPageRepository, ExecutorService executorService) {
        this.fetchPostsProvider = fetchPostsProvider;
        this.fbPageRepository = fbPageRepository;
        this.executorService = executorService;
    }

    public void run(){
        fbPageRepository.getAllFBPageIds().forEach(p -> executorService.submit(()->fetchPostsProvider.get().run(p)));
    }
}
