package ch.uzh.ifi.hase.soprafs23.websocket;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        // "/topic" implies publish-subscribe (one-to-many)
        // "/queue" implies point-to-point (one-to-one) message exchanges
        config.enableSimpleBroker("/topic", "/queue");
        // messages with destination header which starts with "/app" are routed to @MessageMapping in @Controller classes
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // "/envisage" is HTTP URL the endpoint to which the client needs to connect for the webSocket handshake
        registry.addEndpoint("/envisage").withSockJS();
    }
}
