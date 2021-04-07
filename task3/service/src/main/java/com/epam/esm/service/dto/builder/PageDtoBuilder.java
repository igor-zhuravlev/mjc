package com.epam.esm.service.dto.builder;

import com.epam.esm.service.dto.PageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@PropertySource("classpath:pagination.properties")
public class PageDtoBuilder {

    @Value("${pagination.defaultSize}")
    private int defaultSize;
    @Value("${pagination.defaultPage}")
    private int defaultPage;

    public PageDto build(Integer size, Integer page) {
        return new PageDto(Optional.ofNullable(size).orElse(defaultSize),
                Optional.ofNullable(page).orElse(defaultPage));
    }
}
