package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> findAll(Integer offset, Integer limit);
    OrderDto findById(Long id);
    List<OrderDto> findAllByUserId(Long userId);
    OrderDto findByUserId(Long userId, Long orderId);
    OrderDto createOrder(Long userId, OrderDto orderDto);
}
