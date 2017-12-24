package fb.crawler.fb.command;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import fb.crawler.fb.model.Posts;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Slf4j
public class FetchFBPostCommentCommand extends HystrixCommand<Posts.Comments> {
    private URI uri;
    private Client client;

    @Inject
    public FetchFBPostCommentCommand(Client client){
        super(HystrixCommandGroupKey.Factory.asKey("FBPostGroup"), 1000*60);
        this.client = client;
    }

    @Override
    protected Posts.Comments run() throws Exception {
        URI uri = UriBuilder.fromUri(this.uri).build();
        log.debug("Fetching post from {}", uri);
        String res = client.target(uri).request().get(String.class);
        return new ObjectMapper().readValue(res, Posts.Comments.class);
    }

    public FetchFBPostCommentCommand withUri(URI uri){
        this.uri = uri;
        return this;
    }
}
