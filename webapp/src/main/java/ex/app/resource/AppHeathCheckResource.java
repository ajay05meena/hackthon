package ex.app.resource;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/healthcheck")
@Produces(MediaType.APPLICATION_JSON)
public class AppHeathCheckResource {

    public String test(){
        return "App is up";
    }
}
