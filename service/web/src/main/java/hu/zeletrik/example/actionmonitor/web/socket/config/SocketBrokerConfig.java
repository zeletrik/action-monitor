package hu.zeletrik.example.actionmonitor.web.socket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configures the WebSocket message broker.
 */
@Configuration
@EnableWebSocketMessageBroker
public class SocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    private final RelayProperties relayProperties;

    public SocketBrokerConfig(RelayProperties relayProperties) {
        this.relayProperties = relayProperties;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableStompBrokerRelay("/secured/user/queue/specific-user")
                .setRelayHost(relayProperties.getHost())
                .setRelayPort(relayProperties.getPort())
                .setClientLogin(relayProperties.getUsername())
                .setClientPasscode(relayProperties.getPassword());
        config.setApplicationDestinationPrefixes("/chat");
        config.setUserDestinationPrefix("/secured/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket").setAllowedOrigins("*").withSockJS();
    }
}
