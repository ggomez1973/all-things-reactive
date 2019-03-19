package com.ggomez1973.coffee;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
class CoffeeController {
	private final CoffeeService service;

	public CoffeeController(CoffeeService service) {
		this.service = service;
	}

	@GetMapping("/coffees")
	public Flux<Coffee> all(){
		return service.getCoffees();
	}

	@GetMapping("/coffees/{id}")
	public Mono<Coffee> byId(@PathVariable String id){

		return service.getCoffeeById(id)
				.switchIfEmpty(Mono.error(new NoSuchCoffeeException(HttpStatus.BAD_REQUEST,String.format("No such coffee with id: %s", id))));
	}

	@GetMapping(value = "/coffees/{id}/orders", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<CoffeeOrder> orders(@PathVariable String id){
		return service.getOrders(id);
	}
}