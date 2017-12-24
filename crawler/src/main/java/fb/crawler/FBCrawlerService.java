package fb.crawler;


import fb.crawler.fb.FeedData;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;


@Slf4j
public class FBCrawlerService {
    private final Provider<Jedis> jedisProvider;
    private final Provider<FeedData> fetchPostsProvider;
    private final HashMap<String, String> fetchPostLock = new HashMap<>();
    private final static boolean CACHE_ACTIVE = false;

    @Inject
    public FBCrawlerService(Provider<Jedis> jedisProvider, Provider<FeedData> fetchPostsProvider) {
        this.jedisProvider = jedisProvider;
        this.fetchPostsProvider = fetchPostsProvider;
    }





    public String getFBPageDetail(String pageId) {
        if(jedisProvider.get().exists(pageId) && CACHE_ACTIVE){
            return jedisProvider.get().get(pageId);
        }else{
            String detail =fetchPostsProvider.get().pageDetail(pageId);
            jedisProvider.get().set(pageId, detail);
            return detail;
        }
    }

    public void fetchFeedDataForPost(String pageId){
        if(fetchPostLock.containsKey(pageId)){
            return;
        }else{
            fetchPostLock.put(pageId, pageId);
            fetchPostsProvider.get().run(pageId);
            fetchPostLock.remove(pageId);
        }

    }
}
