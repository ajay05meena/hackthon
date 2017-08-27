
import com.google.inject.Guice;
import com.google.inject.Injector;


import fb.crawler.FBCrawler;
import module.FBCrawlerModule;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Application {


    public static void main(String [] args){
        Injector injector = inject();
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        FBCrawler fbCrawler = injector.getInstance(FBCrawler.class);
        Monitoring monitoring = injector.getInstance(Monitoring.class);
        scheduledExecutorService.scheduleAtFixedRate(monitoring, 1 ,5, TimeUnit.SECONDS);

        fbCrawler.run();
        //scheduledExecutorService.shutdownNow();
    }

    private static Injector inject(){
        Injector injector = Guice.createInjector(new FBCrawlerModule());
        //Client client = Client.create();

        //injector.injectMembers(executorService);
        //injector.injectMembers(client);
        return injector;
    }
}
