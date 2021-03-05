package com.epam.esm.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Sort implements Serializable {
    private static final long serialVersionUID = 6957308965809886551L;

    private final List<Order> orders;

    protected Sort(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public static Sort by(List<Order> orders) {
        return new Sort(orders);
    }

    public static class Order implements Serializable {
        private static final long serialVersionUID = -871493463667955567L;

        private final Sort.Direction direction;
        private final String property;

        private Order(Sort.Direction direction, String property) {
            this.direction = direction;
            this.property = property;
        }

        public Direction getDirection() {
            return direction;
        }

        public String getProperty() {
            return property;
        }

        public boolean isAscending() {
            return this.direction.isAscending();
        }

        public boolean isDescending() {
            return this.direction.isDescending();
        }

        public static Order asc(String property) {
            return new Sort.Order(Sort.Direction.ASC, property);
        }

        public static Order desc(String property) {
            return new Sort.Order(Sort.Direction.DESC, property);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Order order = (Order) o;
            return direction == order.direction &&
                    Objects.equals(property, order.property);
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, property);
        }

        @Override
        public String toString() {
            return "Order{" +
                    "direction=" + direction +
                    ", property='" + property + '\'' +
                    '}';
        }
    }

    public static enum Direction {
        ASC, DESC;

        public boolean isAscending() {
            return this.equals(ASC);
        }

        public boolean isDescending() {
            return this.equals(DESC);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sort sort = (Sort) o;
        return Objects.equals(orders, sort.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orders);
    }

    @Override
    public String toString() {
        return "Sort{" +
                "orders=" + orders +
                '}';
    }
}
