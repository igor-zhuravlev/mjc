package com.epam.esm.web.hateoas;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.web.constant.ApiConstant;
import com.epam.esm.web.controller.UserController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserLinkBuilder {

    public void addUserLinks(UserDto userDto) {
        userDto.add(linkTo(methodOn(UserController.class)
                .find(userDto.getId()))
                .withSelfRel());
        userDto.add(linkTo(methodOn(UserController.class)
                .findOrders(userDto.getId(), null, null))
                .withRel(ApiConstant.FIND_ORDERS));
        userDto.add(linkTo(methodOn(UserController.class)
                .findOrder(userDto.getId(), null))
                .withRel(ApiConstant.FIND_ORDER));
    }

    public void addOrdersPagedModelLinks(Long userId, PagedModel<EntityModel<OrderDto>> orderDtoPagedModel) {
        Link findOrderLink = linkTo(methodOn(UserController.class)
                .findOrder(userId, null))
                .withRel(ApiConstant.FIND_ORDER);
        orderDtoPagedModel.add(findOrderLink);
    }

    public void addCreatedOrderLinks(OrderDto orderDto) {
        orderDto.add(linkTo(methodOn(UserController.class)
                .findOrders(orderDto.getId(), null, null))
                .withRel(ApiConstant.FIND_ORDERS));
        orderDto.add(linkTo(methodOn(UserController.class)
                .findOrder(orderDto.getId(), null))
                .withRel(ApiConstant.FIND_ORDER));
    }
}
