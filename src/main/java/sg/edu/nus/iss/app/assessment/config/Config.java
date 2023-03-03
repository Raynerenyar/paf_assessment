package sg.edu.nus.iss.app.assessment.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static sg.edu.nus.iss.app.assessment.Constants.*;

@Configuration
public class Config {

    @Autowired
    Environment env;

    @Bean
    public DataSource dataSource() {
        System.out.println("testing <><><><><><<><><><><>");

        String url;

        // if not on local dev
        if (!(env.containsProperty(LOCAL_DEV))) {

            // get railway's mysql url
            url = env.getProperty(MYSQL_URL);
            int firstIndex = url.indexOf("@");
            int lastIndex = url.lastIndexOf(":");
            String host = url.substring(firstIndex + 1, lastIndex);
            StringBuilder urlBldr = new StringBuilder()
                    .append("jdbc:")
                    .append("mysql://")
                    .append(host)
                    .append(":")
                    .append(env.getProperty(MYSQL_PORT))
                    .append("/")
                    .append(env.getProperty(MYSQL_DATABASE)); // get shared env var
            url = urlBldr.toString();

        } else {
            // for local development
            url = env.getProperty(MYSQL_URL);
        }

        DataSource ds = DataSourceBuilder.create()
                .url(url)
                .password(env.getProperty(MYSQL_PASSOWORD))
                .username(env.getProperty(MYSQL_USER))
                .build();
        System.out.println("jdbctemplate >>>>>>>>>" + ds.toString());
        return ds;
        // return DataSourceBuilder.create()
        //         .url(url)
        //         .password(env.getProperty(MYSQL_PASSOWORD))
        //         .username(env.getProperty(MYSQL_USER))
        //         .build();

    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {

        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();

        config.setHostName(env.getProperty(REDIS_HOST));
        config.setPort(Integer.parseInt(env.getProperty(REDIS_PORT)));

        config.setUsername(env.getProperty(REDIS_USERNAME));
        config.setPassword(env.getProperty(REDIS_PASSWORD));

        config.setDatabase(0);

        // System.out.println(config.getDatabase());
        // System.out.println(config.getUsername());
        // System.out.println(config.getPort());
        // System.out.println(config.getPassword().isPresent());
        final JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();
        final JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
        jedisFac.afterPropertiesSet();

        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

        // associate with redis connection
        redisTemplate.setConnectionFactory(jedisFac);

        redisTemplate.setKeySerializer(new StringRedisSerializer()); // this is for direct key value in redis
        redisTemplate.setValueSerializer(new StringRedisSerializer()); // enable redis to store java object on the value column
        System.out.println("redistemplate >>>>>>>>>>>> " + redisTemplate.toString());
        return redisTemplate;
    };

}
