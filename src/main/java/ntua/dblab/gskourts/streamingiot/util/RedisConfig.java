package ntua.dblab.gskourts.streamingiot.util;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisCommands connectionFactory() {

        String uri = "redis://" + host + ":" + port;
        RedisURI redisURI = RedisURI.create(uri);
        RedisClient redisClient = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> redisConnection = redisClient.connect();
        return redisConnection.sync();
    }
}