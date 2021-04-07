package com.epam.esm.web.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.builder.PageDtoBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.Positive;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    private PageDtoBuilder pageDtoBuilder;

    /**
     * Finds all orders
     * @param size count of orders on page
     * @param page number of page
     * @return list of orders dto
     */

    @PreAuthorize("hasRole(T(com.epam.esm.domain.entity.Role).ADMIN)")
    @GetMapping
    public Page<OrderDto> findAll(@RequestParam(required = false) @Positive Integer size,
                                  @RequestParam(required = false) @Positive Integer page) {
        PageDto pageDto = pageDtoBuilder.build(size, page);
        Page<OrderDto> orderDtoPage = orderService.findAll(pageDto);
        Link selfLink = linkTo(methodOn(OrderController.class)
                .findAll(pageDto.getSize(), pageDto.getPage()))
                .withSelfRel();
        return orderDtoPage;
    }

    /**
     * Search for a order
     * @param id identifier of the order
     * @return found order
     */

    @PreAuthorize("hasRole(T(com.epam.esm.domain.entity.Role).ADMIN)")
    @GetMapping("/{id}")
    public OrderDto find(@PathVariable @Positive Long id) {
        OrderDto orderDto = orderService.findById(id);
        orderDto.add(linkTo(methodOn(OrderController.class)
                .find(id))
                .withSelfRel());
        return orderDto;
    }
}
