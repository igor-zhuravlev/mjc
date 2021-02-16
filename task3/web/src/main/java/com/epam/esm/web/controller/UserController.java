package com.epam.esm.web.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.web.constant.ApiConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        Link selfLink = linkTo(methodOn(UserController.class)
                .findAll(size, page))
                .withSelfRel();
        return CollectionModel.of(userDtoList, selfLink);
    }

    @GetMapping("/{id}")
    public UserDto find(@PathVariable Long id) {
        UserDto userDto = userService.findById(id);
        userDto.add(linkTo(methodOn(UserController.class)
                .find(id))
                .withSelfRel());
        userDto.add(linkTo(methodOn(UserController.class)
                .findOrders(id, null, null))
                .withRel(ApiConstant.FIND_ORDERS));
        userDto.add(linkTo(methodOn(UserController.class)
                .findOrder(id, null))
                .withRel(ApiConstant.FIND_ORDER));
        return userDto;
    }

    @GetMapping("/{userId}/orders")
    public CollectionModel<OrderDto> findOrders(@PathVariable Long userId,
                                     @RequestParam(required = false, defaultValue = "5") Integer size,
                                     @RequestParam(required = false, defaultValue = "1") Integer page) {
        PageDto pageDto = new PageDto(size, page);
        List<OrderDto> orderDtoList = orderService.findAllByUserId(userId, pageDto);
        Link selfLink = linkTo(methodOn(UserController.class)
                .findOrders(userId, size, page))
                .withSelfRel();
        Link findOrderLink = linkTo(methodOn(UserController.class)
                .findOrder(userId, null))
                .withRel(ApiConstant.FIND_ORDER);
        return CollectionModel.of(orderDtoList, selfLink, findOrderLink);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public OrderDto findOrder(@PathVariable Long userId, @PathVariable Long orderId) {
        return orderService.findByUserId(userId, orderId);
    }

    @PostMapping("/{userId}")
    public OrderDto createOrder(@PathVariable Long userId, @RequestBody OrderDto orderDto) {
        OrderDto createdOrderDto = orderService.create(userId, orderDto);
        createdOrderDto.add(linkTo(methodOn(UserController.class)
                .findOrders(userId, null, null))
                .withRel(ApiConstant.FIND_ORDERS));
        createdOrderDto.add(linkTo(methodOn(UserController.class)
                .findOrder(userId, null))
                .withRel(ApiConstant.FIND_ORDER));
        return createdOrderDto;
    }
}
