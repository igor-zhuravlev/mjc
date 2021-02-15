package com.epam.esm.web.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @GetMapping
    public CollectionModel<UserDto> findAll(@RequestParam(required = false, defaultValue = "5") Integer size,
                                            @RequestParam(required = false, defaultValue = "1") Integer page) {
        PageDto pageDto = new PageDto(size, page);
        List<UserDto> userDtoList = userService.findAll(pageDto);
        return CollectionModel.of(userDtoList);
    }

    @GetMapping("/{id}")
    public UserDto find(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/{id}/orders")
    public CollectionModel<OrderDto> findOrders(@PathVariable Long id,
                                     @RequestParam(required = false, defaultValue = "5") Integer size,
                                     @RequestParam(required = false, defaultValue = "1") Integer page) {
        PageDto pageDto = new PageDto(size, page);
        List<OrderDto> orderDtoList = orderService.findAllByUserId(id, pageDto);
        return CollectionModel.of(orderDtoList);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public OrderDto findOrder(@PathVariable Long userId, @PathVariable Long orderId) {
        return orderService.findByUserId(userId, orderId);
    }

    @PostMapping("/{userId}")
    public OrderDto createOrder(@PathVariable Long userId, @RequestBody OrderDto orderDto) {
        return orderService.create(userId, orderDto);
    }
}
