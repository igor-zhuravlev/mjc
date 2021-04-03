package com.epam.esm.web.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.builder.PageDtoBuilder;
import com.epam.esm.web.constant.ApiConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Autowired
    private PageDtoBuilder pageDtoBuilder;

    /**
     * Finds all users
     * @param size count of users on page
     * @param page number of page
     * @return list of users dto
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<UserDto> findAll(@RequestParam(required = false) @Positive Integer size,
                                       @RequestParam(required = false) @Positive Integer page) {
        PageDto pageDto = pageDtoBuilder.build(size, page);
        Page<UserDto> userDtoPage = userService.findAll(pageDto);
        Link selfLink = linkTo(methodOn(UserController.class)
                .findAll(pageDto.getSize(), pageDto.getPage()))
                .withSelfRel();
        return userDtoPage;
    }

    /**
     * Search for a user
     * @param id identifier of the user
     * @return found user
     */

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{id}")
    public UserDto find(@PathVariable @Positive Long id) {
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

    /**
     * Finds all orders by user
     * @param userId identifier of the user
     * @param size count of orders on page
     * @param page number of page
     * @return list of orders dto
     */

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{userId}/orders")
    public Page<OrderDto> findOrders(@PathVariable @Positive Long userId,
                                                @RequestParam(required = false) @Positive Integer size,
                                                @RequestParam(required = false) @Positive Integer page) {
        PageDto pageDto = pageDtoBuilder.build(size, page);
        Page<OrderDto> orderDtoPage = orderService.findAllByUserId(userId, pageDto);
        Link selfLink = linkTo(methodOn(UserController.class)
                .findOrders(userId, pageDto.getSize(), pageDto.getPage()))
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

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
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

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{userId}/orders")
    public OrderDto createOrder(@PathVariable @Positive Long userId,
                                @RequestBody @Valid OrderDto orderDto) {
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
