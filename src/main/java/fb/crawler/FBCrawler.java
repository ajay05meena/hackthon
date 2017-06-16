package fb.crawler;

import datastore.FBPageRepository;
import fb.crawler.post.FetchPosts;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class FBCrawler {
    private final FetchPosts fetchPosts;
    private final FBPageRepository fbPageRepository;

    @Inject
    public FBCrawler(FetchPosts fetchPosts, FBPageRepository fbPageRepository) {
        this.fetchPosts = fetchPosts;
        this.fbPageRepository = fbPageRepository;
    }

    public void run(){
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        try {
            fbPageRepository.getAllFBPageIds().forEach(p -> executorService.submit(()-> fetchPosts.execute(p)));
        } catch (FileNotFoundException e) {
            log.error("Exception {}", e);
            throw new RuntimeException(e);
        }
    }
}
