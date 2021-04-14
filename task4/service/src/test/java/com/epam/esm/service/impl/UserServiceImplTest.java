package com.epam.esm.service.impl;

import com.epam.esm.domain.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.only;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.anyLong;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private final UserService userService = new UserServiceImpl();

    @Test
    void findById_UserExist_ReturnFoundUser() {
        final Long id = 1L;
        final String username = "u1";

        User user = new User();
        user.setId(id);
        user.setUsername(username);

        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setUsername(username);

        given(userRepository.findById(id))
                .willReturn(Optional.of(user));
        given(modelMapper.map(user, UserDto.class))
                .willReturn(userDto);

        userService.findById(id);

        then(userRepository)
                .should(only())
                .findById(anyLong());
        then(modelMapper)
                .should(only())
                .map(any(User.class), eq(UserDto.class));
    }

    @Test
    void findById_UserNotFound_ResourceNotFoundExceptionThrown() {
        final Long id = 1L;

        given(userRepository.findById(id))
                .willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(id);
        });

        then(userRepository)
                .should(only())
                .findById(anyLong());
        then(modelMapper)
                .should(never())
                .map(any(User.class), eq(UserDto.class));
    }
}
