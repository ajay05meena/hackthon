package fb.crawler;


import com.fasterxml.jackson.databind.ObjectMapper;
import cp.redis.RedisClientProvider;
import fb.crawler.fb.FeedData;
import fb.crawler.fb.model.FBPageDetail;
import lombok.extern.slf4j.Slf4j;


import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.util.HashMap;


@Slf4j
public class FBCrawlerService {
    private final RedisClientProvider redisClientProvider;
    private final Provider<FeedData> fetchPostsProvider;
    private final HashMap<String, String> fetchPostLock = new HashMap<>();
    private final static boolean CACHE_ACTIVE = true;
    private final ObjectMapper objectMapper;

    @Inject
    public FBCrawlerService(RedisClientProvider redisClientProvider, Provider<FeedData> fetchPostsProvider, ObjectMapper objectMapper) {
        this.redisClientProvider = redisClientProvider;
        this.fetchPostsProvider = fetchPostsProvider;
        this.objectMapper = objectMapper;
    }





    public FBPageDetail getFBPageDetail(String pageId){
        if(redisClientProvider.get().exists(pageId) && CACHE_ACTIVE){
            return StringToObject(redisClientProvider.get().get(pageId), FBPageDetail.class);
        }else{
            FBPageDetail detail =fetchPostsProvider.get().pageDetail(pageId);
            redisClientProvider.get().set(pageId, objectToString(detail));
            return detail;
        }
    }

    private FBPageDetail StringToObject(String s, Class<FBPageDetail> fbPageDetailClass) {
        try {
            return objectMapper.readValue(s, fbPageDetailClass);
        } catch (IOException e) {
            log.error("Error in reading fbpageDetail object {}", e);
            throw new RuntimeException(e);
        }
    }

    private String objectToString(FBPageDetail fbPageDetail){
        try {
            return objectMapper.writeValueAsString(fbPageDetail);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
