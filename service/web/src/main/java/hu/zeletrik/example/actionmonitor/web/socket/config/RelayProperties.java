package hu.zeletrik.example.actionmonitor.web.socket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "relay")
public class RelayProperties {

    private String host;
    private Integer port;
    private String username;
    private String password;
}
