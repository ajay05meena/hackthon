package example.kafka.consumer;


import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MyKafkaConsumer implements Runnable {
    private final KafkaConsumer kafkaConsumer;

    @Inject
    public MyKafkaConsumer(@Named("kafkaProps") Properties kafkaProperties) {
        this.kafkaConsumer = new KafkaConsumer(kafkaProperties);
    }

    @Override
    public void run() {
        try{
            this.kafkaConsumer.assign(Lists.newArrayList(new TopicPartition("my-replicated-topic", 0)));
            //this.kafkaConsumer.assign(Lists.newArrayList("my-replicated-topic"));
            while (true) {
                ConsumerRecords<String, String> records = this.kafkaConsumer.poll(Long.MAX_VALUE);
                for (ConsumerRecord<String, String> record : records) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("partition", record.partition());
                    data.put("offset", record.offset());
                    data.put("value", record.value());
                    System.out.println(data);
                }
            }
        }finally {
            this.kafkaConsumer.close();
        }

    }

    public void shutdown(){
            this.kafkaConsumer.close();
    }
}


