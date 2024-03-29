package at.ac.fhcampuswien.se.group1.gatewayservice.filter;

import at.ac.fhcampuswien.se.group1.gatewayservice.utility.JwtUtil;
import at.ac.fhcampuswien.se.group1.gatewayservice.utility.RouterValidator;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        log.info(request.getMethodValue() + ": " + request.getPath());

        log.info("Secured endpoint: " + (RouterValidator.isSecured.test(request) ? "true" : "false"));

        if (RouterValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request)) return this.onError(
                    exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);

            final String token = this.getAuthHeader(request);

            String jwt = token.substring(7);

            if (jwtUtil.isInvalid(jwt)) return this.onError(
                    exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);

            this.populateRequestWithHeaders(exchange, jwt);
        } else {
            log.info("No auth configured on this path, letting pass.");
            log.info("Welcome to car rental :D");
        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        log.error(err);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        DecodedJWT decodedJWT = jwtUtil.decodedJWT(token);

        exchange.getRequest().mutate()
                .header("username", decodedJWT.getClaim("username").asString())
                .build();
    }
}

