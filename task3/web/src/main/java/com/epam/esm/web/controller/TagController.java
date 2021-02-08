package com.epam.esm.web.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<TagDto> findAll(@RequestParam(required = false, defaultValue = "0") Integer offset,
                                @RequestParam(required = false, defaultValue = "5") Integer limit) {
        return tagService.findAll(offset, limit);
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
