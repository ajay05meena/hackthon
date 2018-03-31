package datastore;

import fb.crawler.fb.model.AccessToken;

import java.util.List;

public interface FBTokenRepository {
    String getRandomToken();
    void updateToken(AccessToken token);
    List<AccessToken> getAll();
    AccessToken addToken(AccessToken token);
}
