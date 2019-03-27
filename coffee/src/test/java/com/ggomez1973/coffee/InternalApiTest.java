package com.ggomez1973.coffee;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@RunWith(SpringRunner.class)
@WebFluxTest(CoffeeService.class)
public class InternalApiTest {
    @Autowired
    private CoffeeService service;

    @MockBean
    private CoffeeRepository repo;

    Coffee coffee1, coffee2;

    @Before
    public void setUp() throws Exception {
        coffee1 = Coffee.createCoffee("000-TEST-111", "Tester's Choice");
        coffee2 = Coffee.createCoffee("000-TEST-222", "Failgers");

        Mockito.when(repo.findAll()).thenReturn(Flux.just(coffee1, coffee2));
        Mockito.when(repo.findById(coffee1.getId())).thenReturn(Mono.just(coffee1));
        Mockito.when(repo.findById(coffee2.getId())).thenReturn(Mono.just(coffee2));

        Mockito.when(repo.findById("wrong_id")).thenReturn(Mono.empty());
    }

    @Test
    public void getAllCoffees() {
        StepVerifier.withVirtualTime(() -> service.getCoffees())
                .expectNext(coffee1)
                .expectNext(coffee2)
                .verifyComplete();
    }

    @Test
    public void getCoffeeById() {
        StepVerifier.withVirtualTime(() -> service.getCoffeeById(coffee1.getId()))
                .expectNext(coffee1)
                .verifyComplete();
    }

    @Test
    public void getCoffeeByWrongId() {
        // No hago expectNext porque NO HAY NEXT. Retorna un Mono<Void> y da por completo el llamado.
        StepVerifier.withVirtualTime(() -> service.getCoffeeById("wrong_id"))
                .expectNextCount(0)
                .expectComplete();
    }

    @Test
    public void getOrdersForCoffeeById() {
        StepVerifier.withVirtualTime(() -> service.getOrders(coffee2.getId()).take(10))
                .thenAwait(Duration.ofHours(10))
                .expectNextCount(10)
                .verifyComplete();
    }
}