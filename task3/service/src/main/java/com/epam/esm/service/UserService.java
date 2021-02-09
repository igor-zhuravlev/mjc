package com.epam.esm.service;

import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.UserDto;

import java.util.List;

public interface UserService extends Service<UserDto, Long> {
    List<UserDto> findAll(PageDto pageDto);
}
