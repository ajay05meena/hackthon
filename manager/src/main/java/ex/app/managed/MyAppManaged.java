package ex.app.managed;

import com.google.inject.name.Named;
import example.kafka.consumer.MyKafkaConsumer;
import io.dropwizard.lifecycle.Managed;



import javax.inject.Inject;
import java.util.Properties;


public class MyAppManaged implements Managed {
    private final MyKafkaConsumer kafkaConsumer;

    @Inject
    public MyAppManaged(@Named("kafkaProps") Properties kafkaProps) {
        this.kafkaConsumer = new MyKafkaConsumer(kafkaProps);
    }

    @Override
    public void start() throws Exception {
        this.kafkaConsumer.run();
    }

    @Override
    public void stop() throws Exception {
        this.kafkaConsumer.shutdown();
    }
}
