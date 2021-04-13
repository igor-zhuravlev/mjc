package com.epam.esm.web.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.web.constant.ApiConstant;
import com.epam.esm.web.constant.SecurityExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The User Controller represents user api for User
 */

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    /**
     * Finds all users
     * @param page requested page
     * @return list of users dto
     */

    @PreAuthorize(SecurityExpression.HAS_ROLE_ADMIN)
    @GetMapping
    public Page<UserDto> findAll(Pageable page) {
        Page<UserDto> userDtoPage = userService.findAll(page);
        Link selfLink = linkTo(methodOn(UserController.class)
                .findAll(page))
                .withSelfRel();
        return userDtoPage;
    }

    /**
     * Search for a user
     * @param id identifier of the user
     * @return found user
     */

    @GetMapping("/{id}")
    public UserDto find(@PathVariable @Positive Long id) {
        UserDto userDto = userService.findById(id);
        userDto.add(linkTo(methodOn(UserController.class)
                .find(id))
                .withSelfRel());
        userDto.add(linkTo(methodOn(UserController.class)
                .findOrders(id, null))
                .withRel(ApiConstant.FIND_ORDERS));
        userDto.add(linkTo(methodOn(UserController.class)
                .findOrder(id, null))
                .withRel(ApiConstant.FIND_ORDER));
        return userDto;
    }

    /**
     * Finds all orders by user
     * @param userId identifier of the user
     * @param page requested page
     * @return list of orders dto
     */

    @GetMapping("/{userId}/orders")
    public Page<OrderDto> findOrders(@PathVariable @Positive Long userId, Pageable page) {
        Page<OrderDto> orderDtoPage = orderService.findAllByUserId(userId, page);
        Link selfLink = linkTo(methodOn(UserController.class)
                .findOrders(userId, page))
                .withSelfRel();
        Link findOrderLink = linkTo(methodOn(UserController.class)
                .findOrder(userId, null))
                .withRel(ApiConstant.FIND_ORDER);
        return orderDtoPage;
    }

    /**
     * Finds the user's order
     * @param userId identifier of the user
     * @param orderId identifier of the order
     * @return found order dto
     */

    @GetMapping("/{userId}/orders/{orderId}")
    public OrderDto findOrder(@PathVariable @Positive Long userId,
                              @PathVariable @Positive Long orderId) {
        return orderService.findByUserId(userId, orderId);
    }

    /**
     * Creates the user
     * @param userId identifier of the user
     * @param orderDto mapped order dto
     * @return created order dto
     */

    @PreAuthorize(SecurityExpression.HAS_ROLE_USER)
    @PostMapping("/{userId}/orders")
    public OrderDto createOrder(@PathVariable @Positive Long userId,
                                @RequestBody @Valid OrderDto orderDto) {
        OrderDto createdOrderDto = orderService.create(userId, orderDto);
        createdOrderDto.add(linkTo(methodOn(UserController.class)
                .findOrders(userId, null))
                .withRel(ApiConstant.FIND_ORDERS));
        createdOrderDto.add(linkTo(methodOn(UserController.class)
                .findOrder(userId, null))
                .withRel(ApiConstant.FIND_ORDER));
        return createdOrderDto;
    }
}
