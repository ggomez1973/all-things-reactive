package com.ggomez1973.coffee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@SpringBootApplication
public class CoffeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeeApplication.class, args);
	}

}
/*
@RestController
@RequestMapping("/coffees")
class CoffeeController {
	private final CoffeeService service;

	public CoffeeController(CoffeeService service) {
		this.service = service;
	}

	@GetMapping
	public Flux<Coffee> all(){
		return service.getCoffees();
	}

	@GetMapping("/{id}")
	public Mono<Coffee> byId(@PathVariable String id){
		return service.getCoffeeById(id);
	}

	@GetMapping(value = "/{id}/orders", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<CoffeeOrder> orders(@PathVariable String id){
		return service.getOrders(id);
	}
}*/

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


interface CoffeeRepository extends ReactiveMongoRepository<Coffee, String> {}

class CoffeeOrder {
	private String coffeeId;
	private Instant dateOrdered;

	public String getCoffeeId() {
		return coffeeId;
	}

	public Instant getDateOrdered() {
		return dateOrdered;
	}

	private CoffeeOrder(String coffeeId, Instant dateOrdered) {
		this.coffeeId = coffeeId;
		this.dateOrdered = dateOrdered;
	}

	public static CoffeeOrder createCoffeeOrder(String coffeeId, Instant dateOrdered) {
		return new CoffeeOrder(coffeeId, dateOrdered);
	}
}

@Document
class Coffee {
	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	private Coffee(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static Coffee createCoffee(final String id, final String name) {
        return new Coffee(id, name);
    }

    @Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Coffee coffee = (Coffee) o;
		return Objects.equals(id, coffee.id) && Objects.equals(name, coffee.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
}

