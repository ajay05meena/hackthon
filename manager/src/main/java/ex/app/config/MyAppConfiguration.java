package ex.app.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import example.kafka.config.KafkaConfig;
import io.dropwizard.Configuration;
import lombok.Data;
import redis.clients.jedis.JedisPoolConfig;

@Data
public class MyAppConfiguration extends Configuration{
    private String name;
    private JedisPoolConfig jedisPoolConfig;
    private KafkaConfig kafkaConfig;


}
