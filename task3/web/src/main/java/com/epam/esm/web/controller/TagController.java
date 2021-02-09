package com.epam.esm.web.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

/**
 * The Tag Controller represents user api for Tag
 */

@RestController
@RequestMapping("/tags")
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
    public List<TagDto> findAll(@RequestParam(required = false, defaultValue = "5") Integer size,
                                @RequestParam(required = false, defaultValue = "1") Integer page) {
        PageDto pageDto = new PageDto(size, page);
        return tagService.findAll(pageDto);
    }

    /**
     * Search for a tag by name
     * @param id identifier of the tag
     * @return tag dto
     */

    @GetMapping("/{id}")
    public TagDto find(@PathVariable Long id) {
        return tagService.findById(id);
    }

    /**
     * Creates the tag
     * @param tagDto received tag dto
     * @return found tag dto
     */

    @PostMapping
    public TagDto create(@RequestBody TagDto tagDto) {
        return tagService.create(tagDto);
    }

    /**
     * Deletes a tag by id
     * @param id identifier of the tag
     */

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tagService.delete(id);
    }
}
