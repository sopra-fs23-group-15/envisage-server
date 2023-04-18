package ch.uzh.ifi.hase.soprafs23.websocket;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private static final String ORIGIN_LOCALHOST = "http://localhost:3000";
    private static final String ORIGIN_PROD = "https://sopra-fs23-group-15-client.oa.r.appspot.com";
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        // "/topic" implies publish-subscribe (one-to-many)
        // "/queue" implies point-to-point (one-to-one) message exchanges
        config.enableSimpleBroker("/topic", "/queue").setTaskScheduler(heartBeatScheduler());
        // messages with destination header which starts with "/app" are routed to @MessageMapping in @Controller classes
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // "/envisage" is HTTP URL the endpoint to which the client needs to connect for the webSocket handshake
        registry.addEndpoint("/envisage-ws").
                setAllowedOrigins(ORIGIN_LOCALHOST, ORIGIN_PROD).withSockJS();
    }

    @Bean
    public TaskScheduler heartBeatScheduler() {
        // heartbeat to ensure that the connection still exists
        return new ThreadPoolTaskScheduler();
    }
}
