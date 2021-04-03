package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PageDto;
import org.springframework.data.domain.Page;

public interface OrderService extends Service<OrderDto, Long> {
    Page<OrderDto> findAll(PageDto pageDto);
    Page<OrderDto> findAllByUserId(Long userId, PageDto pageDto);
    OrderDto findByUserId(Long userId, Long orderId);
    OrderDto create(Long userId, OrderDto orderDto);
}
