package com.epam.esm.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Sort implements Serializable {
    private static final long serialVersionUID = 6957308965809886551L;

    private final List<Order> orders;

    public static Sort by(List<Order> orders) {
        return new Sort(orders);
    }

    @Data
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Order implements Serializable {
        private static final long serialVersionUID = -871493463667955567L;

        private final Sort.Direction direction;
        private final String property;

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
}
