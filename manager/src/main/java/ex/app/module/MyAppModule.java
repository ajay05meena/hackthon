package ex.app.module;


import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scope;
import ex.app.config.MyAppConfiguration;
import ex.app.resource.AppHeathCheckResource;
import example.kafka.config.KafkaConfig;
import redis.clients.jedis.JedisPool;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class MyAppModule extends AbstractModule {
    @Override
    protected void configure() {

    }


    @Provides
    public JedisPool provideJedisPool(MyAppConfiguration myAppConfiguration){
        return new JedisPool(myAppConfiguration.getJedisPoolConfig(),"localhost");
    }

    @Provides
    public Client provideClient(){
        return ClientBuilder.newClient();
    }

    @Provides
    public KafkaConfig provideKafkaConfig(MyAppConfiguration myAppConfiguration){
        return myAppConfiguration.getKafkaConfig();
    }
}
