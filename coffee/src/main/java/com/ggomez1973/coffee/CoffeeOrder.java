package com.ggomez1973.coffee;

import java.time.Instant;

class CoffeeOrder {
    private String coffeeId;
    private Instant dateOrdered;

    public String getCoffeeId() {
        return coffeeId;
    }

    public Instant getDateOrdered() {
        return dateOrdered;
    }

    public CoffeeOrder() {
    }

    private CoffeeOrder(String coffeeId, Instant dateOrdered) {
        this.coffeeId = coffeeId;
        this.dateOrdered = dateOrdered;
    }

    public static CoffeeOrder createCoffeeOrder(String coffeeId, Instant dateOrdered) {
        return new CoffeeOrder(coffeeId, dateOrdered);
    }
}