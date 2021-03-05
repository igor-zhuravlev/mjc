package com.epam.esm.service.converter.impl;

import com.epam.esm.domain.entity.Order;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.OrderDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderConverter implements Converter<Order, OrderDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Order dtoToEntity(OrderDto orderDto) {
        Order order = new Order();
        modelMapper.map(orderDto, order);
        return order;
    }

    @Override
    public OrderDto entityToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        modelMapper.map(order, orderDto);
        return orderDto;
    }

    @Override
    public List<Order> dtoToEntityList(List<OrderDto> orderDtoList) {
        return orderDtoList.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> entityToDtoList(List<Order> orderList) {
        return orderList.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}
