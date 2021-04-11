package com.epam.esm.service;

import com.epam.esm.service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService extends Service<UserDto, Long> {
    Page<UserDto> findAll(Pageable page);
}
