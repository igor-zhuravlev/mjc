package com.epam.esm.web.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.web.constant.SecurityExpression;
import com.epam.esm.web.hateoas.OrderLinkBuilder;
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
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.Positive;

/**
 * The Order Controller represents user api for Order
 */

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderLinkBuilder orderLinkBuilder;

    /**
     * Finds all orders
     * @param page requested page
     * @param assembler {@link PagedResourcesAssembler} for convert Page into PagedResources
     * @return list of orders dto
     */

    @PreAuthorize(SecurityExpression.HAS_ROLE_ADMIN)
    @GetMapping
    public PagedModel<EntityModel<OrderDto>> findAll(Pageable page, PagedResourcesAssembler<OrderDto> assembler) {
        Page<OrderDto> orderDtoPage = orderService.findAll(page);
        return assembler.toModel(orderDtoPage);
    }

    /**
     * Search for a order
     * @param id identifier of the order
     * @return found order
     */

    @PreAuthorize(SecurityExpression.HAS_ROLE_ADMIN)
    @GetMapping("/{id}")
    public OrderDto find(@PathVariable @Positive Long id) {
        OrderDto orderDto = orderService.findById(id);
        orderLinkBuilder.addOrderLinks(orderDto);
        return orderDto;
    }
}
