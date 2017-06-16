
import com.google.inject.Guice;
import com.google.inject.Injector;

import com.sun.jersey.api.client.Client;
import fb.crawler.post.FetchPosts;

public class Application {


    public static void main(String [] args){
        Injector injector = inject();
        FetchPosts fetchPosts = injector.getInstance(FetchPosts.class);
        fetchPosts.execute("nike");
    }

    private static Injector inject(){
        Injector injector = Guice.createInjector(new DepencyInjector());
        Client client = Client.create();
        injector.injectMembers(client);
        return injector;
    }
}
