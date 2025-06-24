package zulkarnaenfhr.apigateway;

import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class CustomMessageFilter extends AbstractGatewayFilterFactory<CustomMessageFilter.Config> {

    public CustomMessageFilter() {
        super(Config.class);
    }

    @Override
    public org.springframework.cloud.gateway.filter.GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String originalUrl = exchange.getRequest().getURI().toString();

            return chain.filter(exchange).then(Mono.defer(() -> {
                ServerHttpResponse response = exchange.getResponse();
                String customMessage = "Halo, this is the url: " + originalUrl;

                byte[] bytes = customMessage.getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = response.bufferFactory().wrap(bytes);

                response.getHeaders().setContentType(org.springframework.http.MediaType.TEXT_PLAIN);
                return response.writeWith(Flux.just(buffer));
            }));
        };
    }

    public static class Config {
        // Configuration properties (if needed)
    }
}