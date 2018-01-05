package module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import datastore.FBPageRepository;
import datastore.FBPostCommentRepository;
import datastore.FBPostLikeRepository;
import datastore.FBPostRepository;
import datastore.FBTokenRepository;
import datastore.csv.FBPageCsvRepository;
import datastore.csv.FBPostCommentCsvRepository;
import datastore.csv.FBPostCsvRepository;
import datastore.csv.FBPostLikeCsvRepository;
import datastore.memory.FBTokenInMemoryRepository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FBCrawlerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(FBPostRepository.class).toInstance(new FBPostCsvRepository());
        bind(FBPageRepository.class).toInstance(new FBPageCsvRepository());
        bind(FBTokenRepository.class).toInstance(new FBTokenInMemoryRepository());
        bind(FBPostLikeRepository.class).toInstance(new FBPostLikeCsvRepository());
        bind(FBPostCommentRepository.class).toInstance(new FBPostCommentCsvRepository());
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        bind(ExecutorService.class).toInstance(executorService);

    }

    @Provides
    public JedisPool provideJedisPool(){
        return new JedisPool("localhost");
    }

    @Provides
    public Client provideClient(){
        return ClientBuilder.newClient();
    }
}
