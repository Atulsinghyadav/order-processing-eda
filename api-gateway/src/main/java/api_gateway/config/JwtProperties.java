package api_gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
@Component
public class JwtProperties {

    private String secretKey;
    private Long expiration;

}
