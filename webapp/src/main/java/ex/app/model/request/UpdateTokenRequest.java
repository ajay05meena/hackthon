package ex.app.model.request;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;
import fb.crawler.fb.model.AccessToken;

@JsonSnakeCase
@Data
public class UpdateTokenRequest {
    private AccessToken accessToken;
}
