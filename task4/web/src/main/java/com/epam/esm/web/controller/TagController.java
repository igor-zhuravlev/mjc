package com.epam.esm.web.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.web.constant.SecurityExpression;
import com.epam.esm.web.hateoas.TagLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

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
    private TagLinkBuilder tagLinkBuilder;

    /**
     * Finds all tags
     * @param page requested page
     * @param assembler {@link PagedResourcesAssembler} for convert Page into PagedResources
     * @return list of tags dto
     */

    @GetMapping
    public PagedModel<EntityModel<TagDto>> findAll(Pageable page, PagedResourcesAssembler<TagDto> assembler) {
        Page<TagDto> tagDtoPage = tagService.findAll(page);
        return assembler.toModel(tagDtoPage);
    }

    /**
     * Search for a tag by name
     * @param id identifier of the tag
     * @return tag dto
     */


    @GetMapping("/{id}")
    public TagDto find(@PathVariable @Positive Long id) {
        TagDto tagDto = tagService.findById(id);
        tagLinkBuilder.addTagLinks(tagDto);
        return tagDto;
    }

    /**
     * Creates the tag
     * @param tagDto received tag dto
     * @return created tag dto
     */

    @PreAuthorize(SecurityExpression.HAS_ROLE_ADMIN)
    @PostMapping
    public TagDto create(@RequestBody @Valid TagDto tagDto) {
        TagDto createdTagDto = tagService.create(tagDto);
        tagLinkBuilder.addCreatedTagLinks(createdTagDto);
        return createdTagDto;
    }

    /**
     * Deletes a tag by id
     * @param id identifier of the tag
     * @return Object with code 200
     */

    @PreAuthorize(SecurityExpression.HAS_ROLE_ADMIN)
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

    @GetMapping("/users/{userId}/most_used")
    public TagDto findMostWidelyUsedTagWithHighestCostOfOrdersByUserId(@PathVariable @Positive Long userId) {
        TagDto tagDto = tagService.findMostWidelyUsedTagWithHighestCostOfOrdersByUserId(userId);
        tagLinkBuilder.addMostWidelyUsedTagLinks(tagDto, userId);
        return tagDto;
    }
}
