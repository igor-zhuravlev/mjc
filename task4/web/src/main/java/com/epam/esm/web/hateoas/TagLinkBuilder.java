package com.epam.esm.web.hateoas;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.web.constant.ApiConstant;
import com.epam.esm.web.controller.TagController;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagLinkBuilder {

    public void addTagLinks(TagDto tagDto) {
        tagDto.add(linkTo(methodOn(TagController.class)
                .find(tagDto.getId()))
                .withSelfRel());
        tagDto.add(linkTo(methodOn(TagController.class)
                .delete(tagDto.getId()))
                .withRel(ApiConstant.DELETE));
    }

    public void addCreatedTagLinks(TagDto tagDto) {
        tagDto.add(linkTo(methodOn(TagController.class)
                .find(tagDto.getId()))
                .withRel(ApiConstant.FIND));
        tagDto.add(linkTo(methodOn(TagController.class)
                .delete(tagDto.getId()))
                .withRel(ApiConstant.DELETE));
    }

    public void addMostWidelyUsedTagLinks(TagDto tagDto, Long userId) {
        tagDto.add(linkTo(methodOn(TagController.class)
                .findMostWidelyUsedTagWithHighestCostOfOrdersByUserId(userId))
                .withSelfRel());
        tagDto.add(linkTo(methodOn(TagController.class)
                .find(tagDto.getId()))
                .withRel(ApiConstant.FIND));
        tagDto.add(linkTo(methodOn(TagController.class)
                .delete(tagDto.getId()))
                .withRel(ApiConstant.DELETE));
    }
}
