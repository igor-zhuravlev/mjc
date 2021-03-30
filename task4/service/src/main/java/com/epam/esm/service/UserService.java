package com.epam.esm.service;

import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.UserDto;
import org.springframework.data.domain.Page;

public interface UserService extends Service<UserDto, Long> {
    Page<UserDto> findAll(PageDto pageDto);
}
