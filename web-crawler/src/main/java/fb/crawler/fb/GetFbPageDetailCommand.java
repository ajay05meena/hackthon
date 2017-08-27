package fb.crawler.fb;


import com.google.inject.Inject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import java.net.URI;

@Slf4j
public class GetFbPageDetailCommand extends HystrixCommand<String> {
    private final Client client;
    private URI uri;
    @Inject
    protected GetFbPageDetailCommand(Client client) {
        super(HystrixCommandGroupKey.Factory.asKey("hello"));
        this.client = client;
    }

    @Override
    protected String run() throws Exception {
        log.info("Get call {}", this.uri);
        return client.target(this.uri).request(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).get(String.class);

    }

    public GetFbPageDetailCommand withUri(URI uri){
        this.uri = uri;
        return this;
    }
}
