package fb.crawler.fb.command;


import com.google.inject.Inject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import datastore.FBTokenRepository;
import fb.crawler.fb.Constants;
import fb.crawler.fb.model.FBPageDetail;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Slf4j
public class GetFbPageDetailCommand extends HystrixCommand<FBPageDetail> {
    private final Client client;
    private String pageId;
    private final FBTokenRepository fbTokenRepository;
    @Inject
    protected GetFbPageDetailCommand(Client client, FBTokenRepository fbTokenRepository) {
        super(HystrixCommandGroupKey.Factory.asKey("pageDetail"), 10000);
        this.client = client;
        this.fbTokenRepository = fbTokenRepository;
    }

    @Override
    protected FBPageDetail run() throws Exception {
        String token = fbTokenRepository.getRandomToken();
        URI uri = UriBuilder.fromUri(Constants.BASE_URL).path(this.pageId).queryParam("fields", "id,name,likes").queryParam(Constants.ACCESS_TOKEN_LABEL, token).build();
        log.info("Get call {}", uri);
        return client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).get(FBPageDetail.class);

    }

    public GetFbPageDetailCommand withPageId(String pageId){
        this.pageId = pageId;
        return this;
    }
}
