package example.kafka.producer;

import java.util.Properties;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;


public class MyKafkaProducer{
    private Producer producer;
    public MyKafkaProducer(Properties properties){
        this.producer = new KafkaProducer(properties);
    }
    public void publish(String topicName, String message){
        this.producer.send(new ProducerRecord(topicName, message));
    }

}
