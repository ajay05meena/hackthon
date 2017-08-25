package ex.app;


import ex.app.config.MyAppConfiguration;
import ex.app.resource.AppHeathCheckResource;
import io.dropwizard.Application;

import io.dropwizard.setup.Environment;

public class MyApplication extends Application<MyAppConfiguration> {
    @Override
    public void run(MyAppConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(AppHeathCheckResource.class);
    }

    public static void main(String [] args) throws Exception {
        new MyApplication().run(args);
    }

}
