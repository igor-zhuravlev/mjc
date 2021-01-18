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

    @GetMapping("/{name}")
    public TagDto findTagByName(@PathVariable String name) {
        return tagService.findByName(name);
    }

    @PostMapping
    public TagDto saveTag(@RequestBody TagDto tagDto) {
        return tagService.save(tagDto);
    }

    @DeleteMapping("/{name}")
    public void deleteTag(@PathVariable String name) {
        tagService.deleteByName(name);
    }
}
