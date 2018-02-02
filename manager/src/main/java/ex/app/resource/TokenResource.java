package ex.app.resource;

import fb.crawler.fb.model.AccessToken;
import service.TokenService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/token/")
public class TokenResource {
    private final TokenService tokenService;

    @Inject
    public TokenResource(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PUT
    public void updateToken(AccessToken accessToken){tokenService.updateToken(accessToken);
    }

    @POST
    public AccessToken addToken(@Valid AccessToken accessToken){
        return tokenService.addToken(accessToken);
    }

    @DELETE
    public void deleteExpireToken(){
        tokenService.deleteExpiredToken();
    }

    @GET
    public List<AccessToken> getAll(){
        return tokenService.getAllTokens();
    }

}
