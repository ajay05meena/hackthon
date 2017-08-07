package experiment.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GuavaCacheExample {
    static ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    static LoadingCache<String, String> loadingCache = CacheBuilder
            .newBuilder()
            .refreshAfterWrite(100, TimeUnit.MILLISECONDS).build(new CacheLoader<String, String>() {
        @Override
        public String load(String key) throws Exception {
            System.out.println("Loaded from DB" + key);
            Thread.sleep(50);

            return "Value of" + key + " " + System.currentTimeMillis();
        }

        @Override
        public ListenableFuture<String> reload(final String key, String prevValue){
            return executor.submit(new Callable<String>(){
                @Override
                public String call() throws Exception {
                    return load(key);
                }
            });
        }
    });


    public static void main(String [] args) throws ExecutionException, InterruptedException {
        long i = 0;
        loadingCache.get("Key1");
        loadingCache.get("Key2");
        while (i < 1000){
            i++;
            String val = loadingCache.get("key1");
            //ystem.out.println("Val " + val);
            System.out.println("Loop "+ i);
            Thread.sleep(10);
        }



    }

}
