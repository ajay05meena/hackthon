package ex.app.resource;


import com.google.inject.Inject;
import ex.app.model.request.UpdateTokenRequest;
import ex.app.service.AppCrawlerService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/crawler/")
public class CrawlerResource {
    private final AppCrawlerService appCrawlerService;

    @Inject
    public CrawlerResource(AppCrawlerService appCrawlerService) {
        this.appCrawlerService = appCrawlerService;
    }

    @POST
    @Path("token")
    public void updateToken(UpdateTokenRequest request){
        appCrawlerService.updateToken(request);
    }

    @GET
    @Path("page/{fbPageId}/detail")
    public String getFbPageDetail(@PathParam("fbPageId") String pageId){
        return appCrawlerService.getFbPageDetail(pageId);
    }
}
