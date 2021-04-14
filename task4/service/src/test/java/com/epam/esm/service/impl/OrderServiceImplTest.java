package com.epam.esm.service.impl;

import com.epam.esm.domain.entity.Order;
import com.epam.esm.domain.entity.User;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.only;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private final OrderService orderService = new OrderServiceImpl();

    @Test
    void findById_OrderExist_ReturnFoundOrder() {
        final Long id = 1L;

        Order order = new Order();
        order.setId(id);
        OrderDto orderDto = new OrderDto();
        orderDto.setId(id);

        given(orderRepository.findById(id))
                .willReturn(Optional.of(order));
        given(modelMapper.map(order, OrderDto.class))
                .willReturn(orderDto);

        orderService.findById(id);

        then(orderRepository)
                .should(only())
                .findById(anyLong());
        then(modelMapper)
                .should(only())
                .map(any(Order.class), eq(OrderDto.class));
    }

    @Test
    void findById_OrderNotFound_ResourceNotFoundExceptionThrown() {
        final Long id = 1L;

        given(orderRepository.findById(id))
                .willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.findById(id);
        });

        then(orderRepository)
                .should(only())
                .findById(anyLong());
        then(modelMapper)
                .should(never())
                .map(any(Order.class), eq(OrderDto.class));
    }

    @Test
    void findByUserId_UserNotFound_ResourceNotFoundExceptionThrown() {
        final Long userId = 1L;
        final Long orderId = 1L;

        given(userRepository.findById(userId))
                .willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.findByUserId(userId, orderId);
        });

        then(userRepository)
                .should(only())
                .findById(anyLong());
        then(orderRepository)
                .should(never())
                .findByIdAndUser(anyLong(), any(User.class));
        then(modelMapper)
                .should(never())
                .map(any(Order.class), eq(OrderDto.class));
    }

    @Test
    void findByUserId_OrderNotFound_ResourceNotFoundExceptionThrown() {
        final Long userId = 1L;
        final Long orderId = 1L;

        User user = new User();
        user.setId(userId);

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(orderRepository.findByIdAndUser(userId, user))
                .willReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.findByUserId(userId, orderId);
        });

        then(userRepository)
                .should(only())
                .findById(anyLong());
        then(orderRepository)
                .should(only())
                .findByIdAndUser(anyLong(), any(User.class));
        then(modelMapper)
                .should(never())
                .map(any(Order.class), eq(OrderDto.class));
    }

    @Test
    void create_CreatingOrder_ReturnCreatedOrder() {
        final Long userId = 1L;
        OrderDto orderDtoToCreate = new OrderDto();
        Order orderToCreate = new Order();
        orderToCreate.setOrderGiftCertificates(new HashSet<>());

        User user = new User();
        Order savedOrder = new Order();
        OrderDto savedOrderDto = new OrderDto();

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(modelMapper.map(orderDtoToCreate, Order.class))
                .willReturn(orderToCreate);
        given(orderRepository.save(orderToCreate))
                .willReturn(savedOrder);
        given(modelMapper.map(savedOrder, OrderDto.class))
                .willReturn(savedOrderDto);

        orderService.create(userId, orderDtoToCreate);

        then(userRepository)
                .should(only())
                .findById(anyLong());
        then(modelMapper)
                .should(times(1))
                .map(any(OrderDto.class), eq(Order.class));
        then(orderRepository)
                .should(only())
                .save(any(Order.class));
        then(modelMapper)
                .should(times(1))
                .map(any(Order.class), eq(OrderDto.class));
    }

    @Test
    void create_UserNotFound_ResourceNotFoundExceptionThrown() {
        final Long userId = 1L;

        OrderDto orderDtoToCreate = new OrderDto();

        given(userRepository.findById(userId))
                .willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.create(userId, orderDtoToCreate);
        });

        then(userRepository)
                .should(only())
                .findById(anyLong());
        then(modelMapper)
                .should(never())
                .map(any(OrderDto.class), eq(Order.class));
        then(orderRepository)
                .should(never())
                .save(any(Order.class));
        then(modelMapper)
                .should(never())
                .map(any(Order.class), eq(OrderDto.class));
    }
}
