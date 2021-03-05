package com.epam.esm.repository;

import com.epam.esm.domain.entity.Order;
import com.epam.esm.domain.entity.User;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findAllByUser(User user, int offset, int limit);
    Order findByUser(User user, Long orderId);
}
