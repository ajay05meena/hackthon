package datastore.memory;

import datastore.FBTokenRepository;
import fb.crawler.fb.model.AccessToken;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FBTokenInMemoryRepository implements FBTokenRepository {
    private final AtomicInteger id = new AtomicInteger(0);
    private final Map<Integer, AccessToken> tokenMap = new ConcurrentHashMap<>();
    private final Object lock = new Object();
    @Override
    public String getRandomToken() {
        if(tokenMap.isEmpty()){
            throw new RuntimeException("No token is present");
        }
        List<AccessToken> tokens = tokenMap.values().stream().collect(Collectors.toList());
        Collections.shuffle(tokens);
        return tokens.get(0).getTokenAndIncrement();
    }

    @Override
    public void updateToken(AccessToken token) {
        synchronized(lock){
            if(tokenMap.containsKey(token.getId())){
                tokenMap.put(token.getId(), token);
            }else {
                throw new RuntimeException("There is no valid token to update");
            }

        }
    }

    @Override
    public List<AccessToken> getAll() {
        return new ArrayList<>(tokenMap.values());
    }

    @Override
    public AccessToken addToken(AccessToken accessToken) {
        Integer tokenId = id.incrementAndGet();
        accessToken.setId(tokenId);
        return tokenMap.put(tokenId, accessToken);
    }
}
