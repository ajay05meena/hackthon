package ex.app.model;


import io.dropwizard.jackson.JsonSnakeCase;
import lombok.AllArgsConstructor;
import lombok.Data;



@AllArgsConstructor
@JsonSnakeCase
@Data
public class HealthCheckResponse {
    private String message;
    private String appStartTime;
    private Long appUpTime;
}
