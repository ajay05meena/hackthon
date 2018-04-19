package fb.crawler.fb.command;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import datastore.FBTokenRepository;
import fb.crawler.fb.Constants;
import fb.crawler.fb.model.Posts;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Slf4j
public class FetchFBPostCommentCommand extends HystrixCommand<Posts.CommentsWrapper> {
    private URI uri;
    private Client client;
    private final FBTokenRepository fbTokenRepository;

    @Inject
    public FetchFBPostCommentCommand(Client client, FBTokenRepository fbTokenRepository){
        super(HystrixCommandGroupKey.Factory.asKey("FBPostGroup"), 1000*60);
        this.client = client;
        this.fbTokenRepository = fbTokenRepository;
    }

    @Override
    protected Posts.CommentsWrapper run() throws Exception {
        URI uri = UriBuilder.fromUri(this.uri).queryParam(Constants.ACCESS_TOKEN_LABEL, fbTokenRepository.getRandomToken()).build();
        log.debug("Fetching post from {}", uri);
        String res = client.target(uri).request().get(String.class);
        return new ObjectMapper().readValue(res, Posts.CommentsWrapper.class);
    }

    public FetchFBPostCommentCommand withUri(URI uri){
        this.uri = uri;
        return this;
    }
}
