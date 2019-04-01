package com.ggomez1973.coffee;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/*
HttpHandler httpHandler = WebHttpHandlerBuilder.webHandler(toHttpHandler(routerFunction))
  .prependExceptionHandler((serverWebExchange, exception) -> {

       custom handling goes here
      return null;

              }).build();
 */


@Configuration
public class RouteConfig {

    //private static final Logger logger = LoggerFactory.getLogger(RouteConfig.class);
    private static final Logger logger = Loggers.getLogger(RouteConfig.class);
    private final CoffeeService service;

    public RouteConfig(CoffeeService service) {
        this.service = service;
    }

 /*   @Bean public HttpHandler handler() {
        return WebHttpHandlerBuilder.webHandler((WebHandler) toHttpHandler(routerFunction()))
                .exceptionHandler((serverWebExchange, throwable) -> {
                    logger.info(serverWebExchange.toString());
                    logger.error(throwable.toString());
                    return Mono.empty();
                })
                .build();

// https://www.programcreek.com/java-api-examples/?code=saintdan/spring-webflux-microservices-boilerplate/spring-webflux-microservices-boilerplate-master/src/main/java/com/saintdan/framework/Application.java#
// Buen ejemplo con filtrado tipo cuota.
//        return WebHttpHandlerBuilder
//                .webHandler(toHttpHandler(routerFunction())
//                        .prependExceptionHandler()
////                .filter(limitFilter)
////        .filter(validateFilter)
//                .build();
    }
*/

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
        final Mono<Coffee> coffee = service.getCoffeeById(req.pathVariable("id"));
        return coffee
                .flatMap(p -> ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromPublisher(coffee, Coffee.class)))
      /*  return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(service.getCoffeeById(req.pathVariable("id")), Coffee.class)*/
                .switchIfEmpty(Mono.error(new NoSuchCoffeeException(HttpStatus.BAD_REQUEST, "No such coffee")));
        // .switchIfEmpty(notFound().build());
    }


    private Mono<ServerResponse> orders(ServerRequest req) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(service.getOrders(req.pathVariable("id")), CoffeeOrder.class);
    }
}
