package com.ggomez1973.coffee;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

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

    public Coffee() {
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