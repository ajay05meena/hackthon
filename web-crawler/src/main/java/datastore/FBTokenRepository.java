package datastore;

import model.AccessToken;

public interface FBTokenRepository {
    String getRandomToken();
    void updateToken(AccessToken token);
}
