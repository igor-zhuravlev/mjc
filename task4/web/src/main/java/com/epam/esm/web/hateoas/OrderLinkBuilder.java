package com.epam.esm.web.hateoas;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.web.controller.OrderController;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderLinkBuilder {

    public void addOrderLinks(OrderDto orderDto) {
        orderDto.add(linkTo(methodOn(OrderController.class)
                .find(orderDto.getId()))
                .withSelfRel());
    }
}
