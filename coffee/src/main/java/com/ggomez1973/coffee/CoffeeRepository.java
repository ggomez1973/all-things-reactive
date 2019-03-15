package com.ggomez1973.coffee;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface CoffeeRepository extends ReactiveMongoRepository<Coffee, String> {}

