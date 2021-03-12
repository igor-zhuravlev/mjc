package com.epam.esm.repository.impl;

import com.epam.esm.domain.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.config.RepositoryTestConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = RepositoryTestConfig.class)
@Transactional
class UserRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;

    private static int offset;
    private static int limit;

    @BeforeAll
    static void beforeAll() {
        offset = 0;
        limit = 5;
    }

    @Test
    void findAll_SearchForUsersWithOffsetAndLimit_ReturnUserList() {
        List<User> users = userRepository.findAll(offset, limit);
        assertEquals(limit, users.size());
    }

    @Test
    void findById_SearchForUserById_ReturnFoundUser() {
        User user = userRepository.findById(1L);
        assertNotNull(user);
    }
}
