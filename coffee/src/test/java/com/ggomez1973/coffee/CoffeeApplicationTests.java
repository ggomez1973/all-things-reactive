package com.ggomez1973.coffee;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import java.time.Duration;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CoffeeApplicationTests {

	@Autowired
	private CoffeeService service;

	@Test
	public void getOrdersTake10() {
		String coffeeId = service.getCoffees().blockFirst().getId();

		StepVerifier.withVirtualTime(() -> service.getOrders(coffeeId).take(10)) // Back Pressure
                .thenAwait(Duration.ofHours(4))
                .expectNextCount(10)
                .verifyComplete();
	}

	@Test
	public void shouldReturnEmptyIfNotFound(){
		StepVerifier.withVirtualTime(() -> service.getCoffeeById("wrong id"))
				.thenAwait(Duration.ofHours(4))
				.expectNextCount(0)
				.verifyComplete();
	}

}
