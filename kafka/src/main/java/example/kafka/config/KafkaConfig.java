package example.kafka.config;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class KafkaConfig {
    @NotNull
    private String bootstrapServer;
    @NotNull
    private String acks;
    @NotNull
    private String retries;
    @NotNull
    private String batchSize;
    @NotNull
    private String lingerMs;
    @NotNull
    private String keySerializer;
    @NotNull
    private String valueSerializer;
}
