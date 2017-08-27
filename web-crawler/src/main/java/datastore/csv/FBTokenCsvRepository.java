package datastore.csv;

import datastore.FBTokenRepository;
import model.AccessToken;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FBTokenCsvRepository implements FBTokenRepository {
    private final Map<Integer, AccessToken> tokenMap = new ConcurrentHashMap<>();
    private final Object lock = new Object();
    @Override
    public String getRandomToken() {
        if(tokenMap.isEmpty()){
            throw new RuntimeException("No token is present");
        }
        List<String> tokens = tokenMap.values().stream().map(AccessToken::getToken).collect(Collectors.toList());
        Collections.shuffle(tokens);
        return tokens.get(0);
    }

    @Override
    public void updateToken(AccessToken token) {
        synchronized(lock){
            tokenMap.put(token.getId(), token);
        }
    }
}
