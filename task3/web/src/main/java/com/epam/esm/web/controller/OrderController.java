package com.epam.esm.web.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<OrderDto> findAll(@RequestParam(required = false, defaultValue = "5") Integer size,
                                  @RequestParam(required = false, defaultValue = "1") Integer page) {
        PageDto pageDto = new PageDto(size, page);
        return orderService.findAll(pageDto);
    }

    @GetMapping("/{id}")
    public OrderDto find(@PathVariable Long id) {
        return orderService.findById(id);
    }
}
