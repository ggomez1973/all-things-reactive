package com.ggomez1973.coffee;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;

@Configuration
public class RouteConfig {
    private final CoffeeService service;

    public RouteConfig(CoffeeService service) {
        this.service = service;
    }

    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        return route(GET("/coffees"), this::all)
                .andRoute(GET("/coffees/{id}"), this::byId)
                .andRoute(GET("/coffees/{id}/orders"), this::orders);
    }

    private Mono<ServerResponse> all(ServerRequest req) {
        return ServerResponse.ok()
                .body(service.getCoffees(), Coffee.class);
    }

    private Mono<ServerResponse> byId(ServerRequest req) {
        Mono<Coffee> coffee = service.getCoffeeById(req.pathVariable("id"));
        Mono<ServerResponse> sr = notFound().build();
        return ServerResponse.ok()
                .body(coffee, Coffee.class)
                .switchIfEmpty(sr); // Esto no funciona?
    }

    private Mono<ServerResponse> orders(ServerRequest req) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(service.getOrders(req.pathVariable("id")), CoffeeOrder.class);
    }
}
