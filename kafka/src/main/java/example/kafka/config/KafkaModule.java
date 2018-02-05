package example.kafka.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import example.kafka.producer.MyKafkaProducer;

import java.util.Properties;


public class KafkaModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    public MyKafkaProducer kafkaProducer(KafkaConfig kafkaConfig){
        Properties props = kafkaProperties(kafkaConfig);
        return new MyKafkaProducer(props);

    }

    @Provides
    @Named("kafkaProps")
    private Properties kafkaProperties(KafkaConfig kafkaConfig) {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaConfig.getBootstrapServer());
        props.put("acks", kafkaConfig.getAcks());
        props.put("retries", kafkaConfig.getRetries());
        props.put("batch.size", kafkaConfig.getBatchSize());
       // props.put("linger.ms", kafkaConfig.getLingerMs());
        props.put("key.serializer",
                kafkaConfig.getKeySerializer());
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.serializer",
                kafkaConfig.getValueSerializer());
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }
}
