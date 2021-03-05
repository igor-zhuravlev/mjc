package com.epam.esm.service.impl;

import com.epam.esm.domain.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.user.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.only;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.anyLong;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private Converter<User, UserDto> userConverter;

    @InjectMocks
    private final UserService userService = new UserServiceImpl();

    @Test
    void findAll_FoundAllUsers_ReturnListOfUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setLogin("login1");
        user1.setFirstName("firstName1");
        user1.setLastName("lastName1");

        User user2 = new User();
        user2.setId(2L);
        user2.setLogin("login2");
        user2.setFirstName("firstName2");
        user2.setLastName("lastName2");

        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setLogin("login1");
        userDto1.setFirstName("firstName1");
        userDto1.setLastName("lastName1");

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setLogin("login2");
        userDto2.setFirstName("firstName2");
        userDto2.setLastName("lastName2");

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(userDto1);
        userDtoList.add(userDto2);

        PageDto pageDto = new PageDto(5, 1);

        given(userRepository.findAll(pageDto.getOffset(), pageDto.getLimit()))
                .willReturn(userList);
        given(userConverter.entityToDtoList(userList))
                .willReturn(userDtoList);

        List<UserDto> actual = userService.findAll(pageDto);

        assertNotNull(actual);
        assertEquals(userDtoList, actual);

        then(userRepository)
                .should(only())
                .findAll(anyInt(), anyInt());

        then(userConverter)
                .should(only())
                .entityToDtoList(anyList());
    }

    @Test
    void findById_UserExist_ReturnFoundUser() {
        final Long id = 1L;

        User user = new User();
        user.setId(id);
        user.setLogin("login1");
        user.setFirstName("firstName1");
        user.setLastName("lastName1");

        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setLogin("login1");
        userDto.setFirstName("firstName1");
        userDto.setLastName("lastName1");

        given(userRepository.findById(id)).willReturn(user);
        given(userConverter.entityToDto(user)).willReturn(userDto);

        UserDto actual = userService.findById(id);

        assertNotNull(actual);
        assertEquals(userDto, actual);

        then(userRepository)
                .should(only())
                .findById(anyLong());

        then(userConverter)
                .should(only())
                .entityToDto(any(User.class));
    }

    @Test
    void findById_UserNotFound_UserNotFoundExceptionThrown() {
        final Long id = 1L;

        given(userRepository.findById(id)).willReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            userService.findById(id);
        });

        then(userRepository)
                .should(only())
                .findById(anyLong());

        then(userConverter)
                .should(never())
                .entityToDto(any(User.class));
    }
}