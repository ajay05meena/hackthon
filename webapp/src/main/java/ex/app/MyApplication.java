package ex.app;



import com.hubspot.dropwizard.guice.GuiceBundle;
import ex.app.config.MyAppConfiguration;
import ex.app.module.MyAppModule;
import ex.app.resource.AppHeathCheckResource;
import io.dropwizard.Application;


import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MyApplication extends Application<MyAppConfiguration> {
    @Override
    public void run(MyAppConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(AppHeathCheckResource.class);
    }

    @Override
    public void initialize(Bootstrap<MyAppConfiguration> bootstrap){
        GuiceBundle<MyAppConfiguration> guiceBundle = GuiceBundle.<MyAppConfiguration>newBuilder()
                .addModule(new MyAppModule())
                .setConfigClass(MyAppConfiguration.class)
                .build();
        bootstrap.addBundle(guiceBundle);
    }

    public static void main(String [] args) throws Exception {
        new MyApplication().run(args);
    }

}
