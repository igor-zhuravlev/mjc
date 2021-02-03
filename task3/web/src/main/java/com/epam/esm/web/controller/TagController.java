package com.epam.esm.web.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
     * @return list of tags dto
     */

    @GetMapping
    public List<TagDto> findAll() {
        return tagService.findAll();
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
     * Saves the tag
     * @param tagDto received tag dto
     * @return found tag dto
     */

    @PostMapping
    public TagDto save(@RequestBody TagDto tagDto) {
        return tagService.save(tagDto);
    }

    /**
     * Deletes a tag by id
     * @param id identifier of the tag
     */

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tagService.deleteById(id);
    }
}
