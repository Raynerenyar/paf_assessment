package sg.edu.nus.iss.app.assessment.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static sg.edu.nus.iss.app.assessment.Constants.*;

@Configuration
public class MySqlConfig {

    @Autowired
    Environment env;

    @Bean
    public DataSource dataSource() {

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

        return DataSourceBuilder.create()
                .url(url)
                .password(env.getProperty(MYSQL_PASSOWORD))
                .username(env.getProperty(MYSQL_USER))
                .build();

    }

}
