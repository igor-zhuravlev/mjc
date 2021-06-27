package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService extends Service<OrderDto, Long> {
    Page<OrderDto> findAll(Pageable page);
    Page<OrderDto> findAllByUserId(Long userId, Pageable page);
    OrderDto findByUserId(Long userId, Long orderId);
    OrderDto create(Long userId, OrderDto orderDto);
}
