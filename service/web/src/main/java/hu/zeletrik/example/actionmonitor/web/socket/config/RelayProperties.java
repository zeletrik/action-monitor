package hu.zeletrik.example.actionmonitor.web.socket.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Configuration property class to hold message relay related config.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "relay")
public class RelayProperties {

    private String host;
    private Integer port;
    private String username;
    private String password;
}
