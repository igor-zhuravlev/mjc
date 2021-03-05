package com.epam.esm.service.impl;

import com.epam.esm.domain.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.service.constant.ServiceError;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Converter<User, UserDto> userConverter;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll(PageDto pageDto) {
        List<User> users = userRepository.findAll(pageDto.getOffset(), pageDto.getLimit());
        return userConverter.entityToDtoList(users);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new UserNotFoundException(ServiceError.USER_NOT_FOUND.getCode());
        }
        return userConverter.entityToDto(user);
    }
}
