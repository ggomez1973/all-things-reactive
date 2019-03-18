package com.ggomez1973.coffeeclient;

import java.util.Objects;

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

    public Coffee() {
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
