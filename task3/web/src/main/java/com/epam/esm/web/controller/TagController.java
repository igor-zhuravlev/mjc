package com.epam.esm.web.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.web.constant.ApiConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

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

    /**
     * Finds all tags
     * @param size count of gift certificates on page
     * @param page number of page
     * @return list of tags dto
     */

    @GetMapping
    public CollectionModel<TagDto> findAll(@RequestParam(required = false, defaultValue = "5") @Positive Integer size,
                                           @RequestParam(required = false, defaultValue = "1") @Positive Integer page) {
        PageDto pageDto = new PageDto(size, page);
        List<TagDto> tagDtoList = tagService.findAll(pageDto);
        Link selfLink = linkTo(methodOn(TagController.class)
                .findAll(size, page))
                .withSelfRel();
        return CollectionModel.of(tagDtoList, selfLink);
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
     * @return found tag dto
     */

    @PostMapping
    public TagDto create(@RequestBody @Valid TagDto tagDto) {
        TagDto createdTagDto = tagService.create(tagDto);
        createdTagDto.add(linkTo(methodOn(TagController.class)
                .find(createdTagDto.getId()))
                .withRel(ApiConstant.FIND));
        tagDto.add(linkTo(methodOn(TagController.class)
                .delete(createdTagDto.getId()))
                .withRel(ApiConstant.DELETE));
        return createdTagDto;
    }

    /**
     * Deletes a tag by id
     * @param id identifier of the tag
     * @return empty response with code 204
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
