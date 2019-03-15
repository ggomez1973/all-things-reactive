package com.ggomez1973.coffee;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

@Service
class CoffeeService {
    private final CoffeeRepository repo;

    public CoffeeService(CoffeeRepository repo) {
        this.repo = repo;
    }

    public Flux<Coffee> getCoffees(){
        return repo.findAll();
    }

    public Mono<Coffee> getCoffeeById(final String id){
        return repo.findById(id);
    }

    public Flux<CoffeeOrder> getOrders(final String coffeeId){
        return Flux.interval(Duration.ofSeconds(1)).onBackpressureDrop()
                .map(i -> CoffeeOrder.createCoffeeOrder(coffeeId, Instant.now()));
        //	return Flux.<CoffeeOrder>generate(sink -> sink.next(new CoffeeOrder(coffeeId, Instant.now())))
        // 		.delayElements(Duration.ofSeconds(1));
    }
}
