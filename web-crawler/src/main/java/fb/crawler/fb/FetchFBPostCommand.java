package fb.crawler.fb;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;


@Slf4j
public class FetchFBPostCommand extends HystrixCommand<Posts> {
    private URI uri;
    private Client client;

    @Inject
    public FetchFBPostCommand(Client client) {
        super(HystrixCommandGroupKey.Factory.asKey("FBPostGroup"), 1000 * 10);
        this.client = client;
    }

    @Override
    protected Posts run() throws Exception {
        URI uri = UriBuilder.fromUri(this.uri).build();
        log.debug("Fetching post from {}", uri);
        String res = client.target(uri).request().get(String.class);
        return new ObjectMapper().readValue(res, Posts.class);
    }

    public FetchFBPostCommand withUri(URI uri) {
        this.uri = uri;
        return this;
    }
}

