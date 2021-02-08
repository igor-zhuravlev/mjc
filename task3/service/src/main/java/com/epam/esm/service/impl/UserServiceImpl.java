package com.epam.esm.service.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.UserDto;
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
    public List<UserDto> findAll(Integer offset, Integer limit) {
        List<User> users = userRepository.findAll(offset, limit);
        return userConverter.entityToDtoList(users);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id);
        return userConverter.entityToDto(user);
    }
}
