package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;

import java.util.List;

public interface OrderRepository {
    List<Order> findAll(Integer offset, Integer limit);
    Order findById(Long id);
    List<Order> findAllByUser(User user);
    Order findByUser(User user, Long orderId);
    Order saveOrder(Order order);
}
