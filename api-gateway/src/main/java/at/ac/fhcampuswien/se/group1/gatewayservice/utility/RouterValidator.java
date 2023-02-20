package at.ac.fhcampuswien.se.group1.gatewayservice.utility;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    private static final List<String> unsecuredApiEndpoints = List.of(
            "/api/v1/car"
            ,"/api/v1/car/findByStatus"
            ,"/api/v1/currency"
            ,"/api/v1/opening-hours"
            ,"/api/v1/location"
            ,"/api/v1/order"
            ,"/api/v1/auth/admin"
            ,"/api/v1/auth/order"
            ,"/api/v1/car"
    );

    public static final Predicate<ServerHttpRequest> isSecured =
            request -> unsecuredApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri)
                    );
}
