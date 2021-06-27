package com.epam.esm.web.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.web.constant.SecurityExpression;
import com.epam.esm.web.hateoas.UserLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
    private UserLinkBuilder userLinkBuilder;

    /**
     * Finds all users
     * @param page requested page
     * @param assembler {@link PagedResourcesAssembler} for convert Page into PagedResources
     * @return list of users dto
     */

    @PreAuthorize(SecurityExpression.HAS_ROLE_ADMIN)
    @GetMapping
    public PagedModel<EntityModel<UserDto>> findAll(Pageable page, PagedResourcesAssembler<UserDto> assembler) {
        Page<UserDto> userDtoPage = userService.findAll(page);
        return assembler.toModel(userDtoPage);
    }

    /**
     * Search for a user
     * @param id identifier of the user
     * @return found user
     */

    @GetMapping("/{id}")
    public UserDto find(@PathVariable @Positive Long id) {
        UserDto userDto = userService.findById(id);
        userLinkBuilder.addUserLinks(userDto);
        return userDto;
    }

    /**
     * Finds all orders by user
     * @param userId identifier of the user
     * @param page requested page
     * @param assembler {@link PagedResourcesAssembler} for convert Page into PagedResources
     * @return list of orders dto
     */

    @GetMapping("/{userId}/orders")
    public PagedModel<EntityModel<OrderDto>> findOrders(@PathVariable @Positive Long userId, Pageable page,
                                                        PagedResourcesAssembler<OrderDto> assembler) {
        Page<OrderDto> orderDtoPage = orderService.findAllByUserId(userId, page);
        PagedModel<EntityModel<OrderDto>> orderDtoPagedModel = assembler.toModel(orderDtoPage);
        userLinkBuilder.addOrdersPagedModelLinks(userId, orderDtoPagedModel);
        return orderDtoPagedModel;
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
        userLinkBuilder.addCreatedOrderLinks(createdOrderDto);
        return createdOrderDto;
    }
}
