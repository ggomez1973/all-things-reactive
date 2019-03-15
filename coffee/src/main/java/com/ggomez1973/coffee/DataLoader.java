package com.ggomez1973.coffee;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
class DataLoader {
    private final CoffeeRepository repo;

    public DataLoader(CoffeeRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    private void load(){
        repo.deleteAll().thenMany(
                Flux.just("Italiano tostado", "Brasilero", "Egipcio", "Colombiano", "Cafe con leche", "Mierda Americana")
                        .map(name -> Coffee.createCoffee(UUID.randomUUID().toString(), name))
                        .flatMap(repo::save))
                .thenMany(repo.findAll())
                .subscribe(System.out::println);
    }
}
