package com.epam.esm.web.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public CollectionModel<OrderDto> findAll(@RequestParam(required = false, defaultValue = "5") Integer size,
                                             @RequestParam(required = false, defaultValue = "1") Integer page) {
        PageDto pageDto = new PageDto(size, page);
        List<OrderDto> orderDtoList = orderService.findAll(pageDto);
        Link selfLink = linkTo(methodOn(OrderController.class)
                .findAll(size, page))
                .withSelfRel();
        return CollectionModel.of(orderDtoList, selfLink);
    }

    @GetMapping("/{id}")
    public OrderDto find(@PathVariable Long id) {
        OrderDto orderDto = orderService.findById(id);
        orderDto.add(linkTo(methodOn(OrderController.class)
                .find(id))
                .withSelfRel());
        return orderDto;
    }
}
