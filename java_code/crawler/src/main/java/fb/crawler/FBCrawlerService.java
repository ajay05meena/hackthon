package fb.crawler;


import com.fasterxml.jackson.databind.ObjectMapper;
import cp.redis.RedisClientProvider;
import datastore.dao.PostCommentDao;
import datastore.dao.PostDao;
import fb.crawler.fb.FeedData;
import fb.crawler.fb.model.FBPageDetail;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;


import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


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
        FBPageDetail fbPageDetail;
        Jedis jedis =  redisClientProvider.get();
        try {
            if(jedis.exists(pageId) && CACHE_ACTIVE){
                fbPageDetail = stringToObject(jedis.get(pageId));
            }else{
                fbPageDetail =fetchPostsProvider.get().pageDetail(pageId);
                jedis.set(pageId, objectToString(fbPageDetail));
            }
        }catch (Exception e){
            log.error("{}",e);
            fbPageDetail = null;
        }finally {
            jedis.close();
        }
        return fbPageDetail;
    }

    private FBPageDetail stringToObject(String s) {
        try {
            return objectMapper.readValue(s, FBPageDetail.class);
        } catch (IOException e) {
            log.error("Error in reading fbpageDetail object {}", e);
            throw new RuntimeException(e);
        }
    }

    private String objectToString(Object fbPageDetail){
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
            List<PostDao> posts = fetchPostsProvider.get().getFeed(pageId);
            Jedis jedis =  redisClientProvider.get();
            posts.forEach(d -> jedis.sadd("posts", objectToString(d)));
            fetchPostLock.remove(pageId);
            jedis.close();
        }

    }

    public void populateComment() {
        Jedis jedis = redisClientProvider.get();
        String postDaoString = jedis.spop("posts");
        while (!(postDaoString == null || postDaoString.isEmpty())) {
            PostDao postDao = stringToObject(postDaoString, PostDao.class);
            List<PostCommentDao> comments = fetchPostsProvider.get().comments(postDao.getId());
            log.info(postDao.getMessage());
            comments.forEach(c -> jedis.sadd(c.getUserId(), c.getMessage()));
            postDaoString = jedis.spop("posts");
        }
    }

    private PostDao stringToObject(String s, Class<PostDao> fbPageDetailClass) {
        try {
            return objectMapper.readValue(s, fbPageDetailClass);
        } catch (IOException e) {
            log.error("Error in reading fbpageDetail object {}", e);
            throw new RuntimeException(e);
        }
    }
}
