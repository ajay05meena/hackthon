package cp.redis;


import com.google.inject.Inject;
import com.google.inject.Provider;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisClientProvider implements Provider<Jedis> {
    private final JedisPool jedisPool;

    @Inject
    public RedisClientProvider(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public Jedis get() {
        return jedisPool.getResource();
    }
}
