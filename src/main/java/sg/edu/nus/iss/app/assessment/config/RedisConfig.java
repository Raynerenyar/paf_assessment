package sg.edu.nus.iss.app.assessment.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import static sg.edu.nus.iss.app.assessment.Constants.*;

@Configuration
public class RedisConfig {

    @Autowired
    Environment env;

    @Bean
    @Scope("singleton")
    public RedisTemplate<String, Object> redisTemp() {

        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();

        config.setHostName(env.getProperty(REDIS_HOST));
        config.setPort(Integer.parseInt(env.getProperty(REDIS_PORT)));
        // config.setPort(REDIS_PORT.get());

        // if (!REDIS_HOST.equalsIgnoreCase("localhost")) {
        //     config.setUsername(env.getProperty(REDIS_USERNAME));
        //     config.setPassword(env.getProperty(REDIS_PASSWORD));
        // }
        config.setUsername(env.getProperty(REDIS_USERNAME));
        config.setPassword(env.getProperty(REDIS_PASSWORD));

        config.setDatabase(0);

        final JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();
        final JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
        jedisFac.afterPropertiesSet();

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // associate with redis connection
        redisTemplate.setConnectionFactory(jedisFac);

        // not necessary to set this as it defaults to string
        // StringRedisSerializer converts String into bytes as Redis stores data in
        // bytes. It can also do the reverse.
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // this is for direct key value in redis
        // redisTemplate.setValueSerializer(new StringRedisSerializer()); // this is for direct key value in redis

        // enable redis map key to store String
        // usually default is string
        // redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // RedisSerializer<Object> objSerializer = new JdkSerializationRedisSerializer(getClass().getClassLoader());

        // redisTemplate.setHashValueSerializer(objSerializer);

        // redisTemplate.setValueSerializer(objSerializer); // enable redis to store java object on the value column
        redisTemplate.setValueSerializer(new StringRedisSerializer()); // enable redis to store java object on the value column
        return redisTemplate;
    };

}
