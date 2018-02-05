package example.kafka;

import example.kafka.consumer.MyKafkaConsumer;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("kafka")
@Slf4j
public class KafkaResource {
    private final Provider<MyKafkaConsumer> myKafkaConsumerProvider;
    private boolean started =false;

    @Inject
    public KafkaResource(Provider<MyKafkaConsumer> myKafkaConsumerProvider) {

        this.myKafkaConsumerProvider = myKafkaConsumerProvider;
    }

    @POST
    @Path("/start")
    public void start(){
        if(!started){
            new Thread(myKafkaConsumerProvider.get()).start();
            started = true;
        }else{
            log.error("already running");
        }
    }



}
