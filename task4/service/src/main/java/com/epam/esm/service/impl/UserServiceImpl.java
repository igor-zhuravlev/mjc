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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Converter<User, UserDto> userConverter;

    @Transactional(readOnly = true)
    @Override
    public Page<UserDto> findAll(PageDto pageDto) {
        Page<User> users = userRepository.findAll(PageRequest.of(pageDto.getPage(), pageDto.getSize()));
        return users.map(userConverter::entityToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElseThrow(() ->
                new UserNotFoundException(ServiceError.USER_NOT_FOUND.getCode()));
        return userConverter.entityToDto(user);
    }
}
