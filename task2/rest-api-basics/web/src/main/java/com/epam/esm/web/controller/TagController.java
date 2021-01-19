package com.epam.esm.web.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public List<TagDto> findAllTags() {
        return tagService.findAll();
    }

    @GetMapping("/{id}")
    public TagDto findTagById(@PathVariable Long id) {
        return tagService.findById(id);
    }

    @PostMapping
    public TagDto saveTag(@RequestBody TagDto tagDto) {
        return tagService.save(tagDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTagById(@PathVariable Long id) {
        tagService.deleteById(id);
    }
}
