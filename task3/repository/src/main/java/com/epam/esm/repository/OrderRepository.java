package com.epam.esm.repository;

import com.epam.esm.entity.Order;

import java.util.List;

public interface OrderRepository {
    List<Order> findAll();
    Order findById(Long id);
    List<Order> findByUserId(Long id);
    Order saveOrder(Order order);
}
