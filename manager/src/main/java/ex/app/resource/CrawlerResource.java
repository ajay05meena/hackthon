package ex.app.resource;


import com.google.inject.Inject;
import fb.crawler.fb.model.AccessToken;
import service.AppCrawlerService;
import service.TokenService;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/crawler/")
public class CrawlerResource {
    private final AppCrawlerService appCrawlerService;


    @Inject
    public CrawlerResource(AppCrawlerService appCrawlerService) {
        this.appCrawlerService = appCrawlerService;
    }


    @GET
    @Path("page/{fbPageId}/detail")
    public String getFbPageDetail(@PathParam("fbPageId") String pageId){
        return appCrawlerService.getFbPageDetail(pageId);
    }
}
