package service;


import datastore.FBTokenRepository;
import fb.crawler.fb.model.AccessToken;

import javax.inject.Inject;
import java.util.List;

public class TokenService {
    private final FBTokenRepository fbTokenRepository;

    @Inject
    public TokenService(FBTokenRepository fbTokenRepository) {
        this.fbTokenRepository = fbTokenRepository;
    }

    public void deleteExpiredToken() {
        throw new RuntimeException("Not implement yet");
    }

    public void updateToken(AccessToken accessToken) {
        fbTokenRepository.updateToken(accessToken);
    }

    public List<AccessToken> getAllTokens(){
        return fbTokenRepository.getAll();
    }

    public AccessToken addToken(AccessToken accessToken){
        return fbTokenRepository.addToken(accessToken);
    }


}
