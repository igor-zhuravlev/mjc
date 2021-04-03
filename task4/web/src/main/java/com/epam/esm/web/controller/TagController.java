package com.epam.esm.web.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.builder.PageDtoBuilder;
import com.epam.esm.web.constant.ApiConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The Tag Controller represents user api for Tag
 */

@RestController
@RequestMapping("/tags")
@Validated
public class TagController {

    @Autowired
    private TagService tagService;
    @Autowired
    private PageDtoBuilder pageDtoBuilder;

    /**
     * Finds all tags
     * @param size count of gift certificates on page
     * @param page number of page
     * @return list of tags dto
     */

    @GetMapping
    public Page<TagDto> findAll(@RequestParam(required = false) @Positive Integer size,
                                           @RequestParam(required = false) @Positive Integer page) {
        PageDto pageDto = pageDtoBuilder.build(size, page);
        Page<TagDto> tagDtoPage = tagService.findAll(pageDto);
        Link selfLink = linkTo(methodOn(TagController.class)
                .findAll(pageDto.getSize(), pageDto.getPage()))
                .withSelfRel();
        return tagDtoPage;
    }

    /**
     * Search for a tag by name
     * @param id identifier of the tag
     * @return tag dto
     */

    @GetMapping("/{id}")
    public TagDto find(@PathVariable @Positive Long id) {
        TagDto tagDto = tagService.findById(id);
        tagDto.add(linkTo(methodOn(TagController.class)
                .find(id))
                .withSelfRel());
        tagDto.add(linkTo(methodOn(TagController.class)
                .delete(id))
                .withRel(ApiConstant.DELETE));
        return tagDto;
    }

    /**
     * Creates the tag
     * @param tagDto received tag dto
     * @return created tag dto
     */

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public TagDto create(@RequestBody @Valid TagDto tagDto) {
        TagDto createdTagDto = tagService.create(tagDto);
        createdTagDto.add(linkTo(methodOn(TagController.class)
                .find(createdTagDto.getId()))
                .withRel(ApiConstant.FIND));
        createdTagDto.add(linkTo(methodOn(TagController.class)
                .delete(createdTagDto.getId()))
                .withRel(ApiConstant.DELETE));
        return createdTagDto;
    }

    /**
     * Deletes a tag by id
     * @param id identifier of the tag
     * @return Object with code 200
     */

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long id) {
        tagService.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Finds the most widely used tag of a user with the highest cost of all orders
     * @param userId identifier of the user
     * @return found tag dto
     */

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/users/{userId}/most_used")
    public TagDto findMostWidelyUsedTagWithHighestCostOfOrdersByUserId(@PathVariable @Positive Long userId) {
        TagDto tagDto = tagService.findMostWidelyUsedTagWithHighestCostOfOrdersByUserId(userId);
        tagDto.add(linkTo(methodOn(TagController.class)
                .findMostWidelyUsedTagWithHighestCostOfOrdersByUserId(userId))
                .withSelfRel());
        tagDto.add(linkTo(methodOn(TagController.class)
                .find(tagDto.getId()))
                .withRel(ApiConstant.FIND));
        tagDto.add(linkTo(methodOn(TagController.class)
                .delete(tagDto.getId()))
                .withRel(ApiConstant.DELETE));
        return tagDto;
    }
}
