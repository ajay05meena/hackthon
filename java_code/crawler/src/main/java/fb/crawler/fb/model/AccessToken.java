package fb.crawler.fb.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;


@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessToken {
    @Getter @Setter
    private int id;
    @Setter
    private String token;
    @Getter
    private AtomicInteger inUseCount = new AtomicInteger(0);

    public String getTokenAndIncrement(){
        inUseCount.getAndIncrement();
        return token;
    }
}
