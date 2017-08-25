package ex.app.config;


import io.dropwizard.Configuration;
import lombok.Data;

@Data
public class MyAppConfiguration extends Configuration{

    private String name;


}
