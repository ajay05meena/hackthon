package datastore;

import fb.crawler.fb.model.AccessToken;

public interface FBTokenRepository {
    String getRandomToken();
    void updateToken(AccessToken token);
}
