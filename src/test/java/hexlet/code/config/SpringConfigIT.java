package hexlet.code.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import static hexlet.code.config.SpringConfigIT.TEST_PROFILE;

@Configuration
@Profile(TEST_PROFILE)
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "hexlet.code")
@PropertySource(value = "classpath:/config/application.yml")
public class SpringConfigIT {
    public static final String LOGIN = "/api/login";
    public static final String TEST_PROFILE = "test";

    /**
     * Get rest template.
     * @return      new rest template
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }
}
