package fb.crawler.post;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.sun.jersey.api.client.Client;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Slf4j
public class FetchFBPostLikeCommand extends HystrixCommand<Posts.Likes>{
    private URI uri;
    private Client client;

    @Inject
    public FetchFBPostLikeCommand(Client client){
        super(HystrixCommandGroupKey.Factory.asKey("FBPostGroup"), 1000*60);
        this.client = client;
    }

    @Override
    protected Posts.Likes run() throws Exception {
        URI uri = UriBuilder.fromUri(this.uri).build();
        log.debug("Fetching post from {}", uri);
        String res = client.resource(uri).get(String.class);
        return new ObjectMapper().readValue(res, Posts.Likes.class);
    }

    public FetchFBPostLikeCommand withUri(URI uri){
        this.uri = uri;
        return this;
    }
}
