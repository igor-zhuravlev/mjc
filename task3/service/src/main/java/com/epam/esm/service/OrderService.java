package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PageDto;

import java.util.List;

public interface OrderService extends Service<OrderDto, Long> {
    List<OrderDto> findAll(PageDto pageDto);
    List<OrderDto> findAllByUserId(Long userId, PageDto pageDto);
    OrderDto findByUserId(Long userId, Long orderId);
    OrderDto create(Long userId, OrderDto orderDto);
}
