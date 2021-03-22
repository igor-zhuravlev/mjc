package com.epam.esm.service.converter.impl;

import com.epam.esm.domain.entity.User;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter implements Converter<User, UserDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public User dtoToEntity(UserDto userDto) {
        User user = new User();
        modelMapper.map(userDto, user);
        return user;
    }

    @Override
    public UserDto entityToDto(User user) {
        UserDto userDto = new UserDto();
        modelMapper.map(user, userDto);
        return userDto;
    }

    @Override
    public List<User> dtoToEntityList(List<UserDto> userDtoList) {
        return userDtoList.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> entityToDtoList(List<User> userList) {
        return userList.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}
