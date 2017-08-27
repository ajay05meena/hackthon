package ex.app.resource;

import com.google.inject.Inject;
import ex.app.model.HealthCheckResponse;
import ex.app.service.MyAppService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/healthcheck/")
@Produces(MediaType.APPLICATION_JSON)
public class AppHeathCheckResource {
    private final MyAppService myAppService;

    @Inject
    public AppHeathCheckResource(MyAppService myAppService) {
        this.myAppService = myAppService;
    }

    @GET
    public HealthCheckResponse healthCheck(){
        return myAppService.healthCheck();
    }
}
