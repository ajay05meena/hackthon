package ex.app.resource;


import com.google.inject.Inject;
import fb.crawler.fb.model.AccessToken;
import fb.crawler.fb.model.FBPageDetail;
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
import java.io.IOException;
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
    public FBPageDetail getFbPageDetail(@PathParam("fbPageId") String pageId){
        return appCrawlerService.getFbPageDetail(pageId);
    }

    @GET
    @Path("page/{fbPageId}/feed")
    public String getFBPageFeed(@PathParam("fbPageId") String pageId){
        return appCrawlerService.getFbPageFeed(pageId);
    }

    @POST
    @Path("/post/comment/populator")
    public void populateComments(){
        appCrawlerService.populateComment();
    }

}
