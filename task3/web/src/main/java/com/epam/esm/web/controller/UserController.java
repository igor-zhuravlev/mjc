package com.epam.esm.web.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<UserDto> findAll(@RequestParam(required = false, defaultValue = "0") Integer offset,
                                 @RequestParam(required = false, defaultValue = "5") Integer limit) {
        return userService.findAll(offset, limit);
    }

    @GetMapping("/{id}")
    public UserDto find(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/{id}/orders")
    public List<OrderDto> findOrders(@PathVariable Long id) {
        return orderService.findAllByUserId(id);
    }

    @GetMapping("/{id}/orders/{orderId}")
    public OrderDto findOrder(@PathVariable Long id, @PathVariable Long orderId) {
        return orderService.findByUserId(id, orderId);
    }

    @PostMapping("/{id}")
    public OrderDto createOrder(@PathVariable Long id, @RequestBody OrderDto orderDto) {
        return orderService.createOrder(id, orderDto);
    }
}
