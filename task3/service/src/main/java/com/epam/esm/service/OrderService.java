package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> findAll();
    OrderDto findById(Long id);
    List<OrderDto> findByUserId(Long id);
    OrderDto placeOrder(Long userId, OrderDto orderDto);
}
