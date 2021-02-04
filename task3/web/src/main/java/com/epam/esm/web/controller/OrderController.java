package com.epam.esm.web.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<OrderDto> findAll() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public OrderDto findById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @GetMapping("/users/{id}")
    public List<OrderDto> findByUserId(@PathVariable Long id) {
        return orderService.findByUserId(id);
    }

    @PostMapping("/users/{id}")
    public OrderDto placeOrder(@PathVariable Long id, @RequestBody OrderDto orderDto) {
        return orderService.placeOrder(id, orderDto);
    }
}
