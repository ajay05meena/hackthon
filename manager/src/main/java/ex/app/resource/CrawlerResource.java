package ex.app.resource;


import com.google.inject.Inject;
import example.kafka.producer.MyKafkaProducer;
import fb.crawler.fb.model.FBPageDetail;
import service.AppCrawlerService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/crawler/")
public class CrawlerResource {
    private final AppCrawlerService appCrawlerService;
    private final MyKafkaProducer myKafkaProducer;


    @Inject
    public CrawlerResource(AppCrawlerService appCrawlerService, MyKafkaProducer myKafkaProducer) {
        this.appCrawlerService = appCrawlerService;
        this.myKafkaProducer = myKafkaProducer;
    }


    @GET
    @Path("page/{fbPageId}/detail")
    public FBPageDetail getFbPageDetail(@PathParam("fbPageId") String pageId){
        FBPageDetail detail =  appCrawlerService.getFbPageDetail(pageId);
        myKafkaProducer.publish("my-replicated-topic", detail.toString());
        return detail;
    }
}
