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
    public List<OrderDto> findAll(@RequestParam(required = false, defaultValue = "0") Integer offset,
                                  @RequestParam(required = false, defaultValue = "5") Integer limit) {
        return orderService.findAll(offset, limit);
    }

    @GetMapping("/{id}")
    public OrderDto findById(@PathVariable Long id) {
        return orderService.findById(id);
    }
}
