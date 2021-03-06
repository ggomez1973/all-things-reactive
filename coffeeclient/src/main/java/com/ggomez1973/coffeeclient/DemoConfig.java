package com.ggomez1973.coffeeclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DemoConfig {
    @Bean
    WebClient client() {
        // Points to Java-based coffee service configured to run on 8080
        return WebClient.create("http://localhost:8080/coffees/888");
    }

    @Bean
    CommandLineRunner demo(WebClient client) {
        return args -> client.get()
                .retrieve()
                .bodyToMono(Coffee.class)

//                .bodyToFlux(Coffee.class)
//                .filter(coffee -> coffee.getName().equalsIgnoreCase("Italiano tostade"))
                .subscribe(System.out::println);

                /*
                .flatMap(coffee -> client.get()
                        .uri("/{id}/orders", coffee.getId())
                        .retrieve()
                        .bodyToFlux(CoffeeOrder.class))
                .subscribe(System.out::println);*/
    }
}