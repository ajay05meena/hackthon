package fb.crawler;

import datastore.FBPageRepository;
import datastore.FBTokenRepository;
import fb.crawler.fb.FetchPosts;
import lombok.extern.slf4j.Slf4j;
import model.AccessToken;
import redis.clients.jedis.Jedis;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.ExecutorService;


@Slf4j
public class FBCrawler {
    private final Provider<Jedis> jedisProvider;
    private final Provider<FetchPosts> fetchPostsProvider;
    private final FBPageRepository fbPageRepository;
    private final ExecutorService executorService;
    private final FBTokenRepository fbTokenRepository;

    @Inject
    public FBCrawler(Provider<Jedis> jedisProvider, Provider<FetchPosts> fetchPostsProvider, FBPageRepository fbPageRepository, ExecutorService executorService, FBTokenRepository fbTokenRepository) {
        this.jedisProvider = jedisProvider;
        this.fetchPostsProvider = fetchPostsProvider;
        this.fbPageRepository = fbPageRepository;
        this.executorService = executorService;
        this.fbTokenRepository = fbTokenRepository;
    }

    public void run(){
        //fbPageRepository.getAllFBPageIds().forEach(p -> executorService.submit(()->fetchPostsProvider.get().run(p)));
    }

    public void updateToken(AccessToken accessToken) {
        fbTokenRepository.updateToken(accessToken);
    }

    public String getFBPageDetail(String pageId) {
        if(jedisProvider.get().exists(pageId)){
            return jedisProvider.get().get(pageId) + "from cache";
        }else{
            String detail =fetchPostsProvider.get().pageDetail(pageId);
            jedisProvider.get().set(pageId, detail);
            return detail;
        }

    }
}
