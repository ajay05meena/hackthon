package ex.app.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Configuration;
import lombok.Data;

@Data
public class MyAppConfiguration extends Configuration{
    private String name;


}
