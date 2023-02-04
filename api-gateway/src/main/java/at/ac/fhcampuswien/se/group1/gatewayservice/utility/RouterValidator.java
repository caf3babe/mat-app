package at.ac.fhcampuswien.se.group1.gatewayservice.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    @Value("${gateway-config.secured-get-paths}")
    private static List<String> openApiEndpointsGet;
    @Value("${gateway-config.secured-post-paths}")
    private static List<String> openApiEndpointsPost;

    public static final Predicate<ServerHttpRequest> isSecuredGet =
            request -> openApiEndpointsGet
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri) && Objects.equals(request.getMethod(), HttpMethod.GET)
                    );

    public static final Predicate<ServerHttpRequest> isSecuredPost =
            request -> openApiEndpointsPost
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri) && Objects.equals(request.getMethod(), HttpMethod.POST)
                    );
}
