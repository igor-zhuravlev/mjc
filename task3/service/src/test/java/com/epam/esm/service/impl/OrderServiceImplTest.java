package com.epam.esm.service.impl;

import com.epam.esm.domain.entity.Order;
import com.epam.esm.domain.entity.User;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.exception.order.OrderNotFoundException;
import com.epam.esm.service.exception.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
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
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Converter<Order, OrderDto> orderConverter;

    @InjectMocks
    private final OrderService orderService = new OrderServiceImpl();

    private static List<Order> orderList;
    private static List<OrderDto> orderDtoList;

    @BeforeAll
    static void beforeAll() {
        orderList = new ArrayList<>();
        orderDtoList = new ArrayList<>();

        for (long i = 1; i <= 2; i++) {
            Order order = new Order();
            order.setId(i);

            OrderDto orderDto = new OrderDto();
            orderDto.setId(i);

            orderList.add(order);
            orderDtoList.add(orderDto);
        }
    }

    @Test
    void findAll_FoundAllOrders_ReturnListOfOrders() {
        PageDto pageDto = new PageDto(5, 1);

        given(orderRepository.findAll(pageDto.getOffset(), pageDto.getLimit()))
                .willReturn(orderList);
        given(orderConverter.entityToDtoList(orderList))
                .willReturn(orderDtoList);

        List<OrderDto> actual = orderService.findAll(pageDto);

        assertNotNull(actual);
        assertEquals(orderDtoList, actual);

        then(orderRepository)
                .should(only())
                .findAll(anyInt(), anyInt());

        then(orderConverter)
                .should(only())
                .entityToDtoList(anyList());
    }

    @Test
    void findById_OrderExist_ReturnFoundOrder() {
        final Long id = 1L;

        Order order = new Order();
        order.setId(id);
        OrderDto orderDto = new OrderDto();
        orderDto.setId(id);

        given(orderRepository.findById(id)).willReturn(order);
        given(orderConverter.entityToDto(order)).willReturn(orderDto);

        OrderDto actual = orderService.findById(id);

        assertNotNull(actual);
        assertEquals(orderDto, actual);

        then(orderRepository)
                .should(only())
                .findById(anyLong());

        then(orderConverter)
                .should(only())
                .entityToDto(any(Order.class));
    }

    @Test
    void findById_OrderNotFound_OrderNotFoundExceptionThrown() {
        final Long id = 1L;

        given(orderRepository.findById(id)).willReturn(null);

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.findById(id);
        });

        then(orderRepository)
                .should(only())
                .findById(anyLong());

        then(orderConverter)
                .should(never())
                .entityToDto(any(Order.class));
    }

    @Test
    void findAllByUserId_FoundAllOrders_ReturnListOfUserOrders() {
        final Long userId = 1L;
        PageDto pageDto = new PageDto(5, 1);

        User user = new User();
        user.setId(userId);

        given(userRepository.findById(userId))
                .willReturn(user);
        given(orderRepository.findAllByUser(user, pageDto.getOffset(), pageDto.getLimit()))
                .willReturn(orderList);
        given(orderConverter.entityToDtoList(orderList))
                .willReturn(orderDtoList);

        List<OrderDto> actual = orderService.findAllByUserId(userId, pageDto);

        assertNotNull(actual);
        assertEquals(orderDtoList, actual);

        then(userRepository)
                .should(only())
                .findById(anyLong());

        then(orderRepository)
                .should(only())
                .findAllByUser(any(User.class), anyInt(), anyInt());

        then(orderConverter)
                .should(only())
                .entityToDtoList(anyList());
    }

    @Test
    void findAllByUserId_UserNotFound_UserNotFoundExceptionThrown() {
        final Long userId = 1L;
        PageDto pageDto = new PageDto(5, 1);

        given(userRepository.findById(userId))
                .willReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            orderService.findAllByUserId(userId, pageDto);
        });

        then(userRepository)
                .should(only())
                .findById(anyLong());

        then(orderRepository)
                .should(never())
                .findAllByUser(any(User.class), anyInt(), anyInt());

        then(orderConverter)
                .should(never())
                .entityToDtoList(anyList());
    }

    @Test
    void findByUserId_FoundOrder_ReturnOrderByUserIdAndOrderId() {
        final Long userId = 1L;
        final Long orderId = 1L;

        User user = new User();
        user.setId(userId);

        Order order = new Order();
        order.setId(orderId);
        OrderDto orderDto = new OrderDto();
        orderDto.setId(orderId);

        given(userRepository.findById(userId))
                .willReturn(user);
        given(orderRepository.findByUser(user, orderId))
                .willReturn(order);
        given(orderConverter.entityToDto(order))
                .willReturn(orderDto);

        OrderDto actual = orderService.findByUserId(userId, orderId);

        assertNotNull(actual);
        assertEquals(orderDto, actual);

        then(userRepository)
                .should(only())
                .findById(anyLong());

        then(orderRepository)
                .should(only())
                .findByUser(any(User.class), anyLong());

        then(orderConverter)
                .should(only())
                .entityToDto(any(Order.class));
    }

    @Test
    void findByUserId_UserNotFound_UserNotFoundExceptionThrown() {
        final Long userId = 1L;
        final Long orderId = 1L;

        given(userRepository.findById(userId))
                .willReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            orderService.findByUserId(userId, orderId);
        });

        then(userRepository)
                .should(only())
                .findById(anyLong());

        then(orderRepository)
                .should(never())
                .findByUser(any(User.class), anyLong());

        then(orderConverter)
                .should(never())
                .entityToDto(any(Order.class));
    }

    @Test
    void findByUserId_OrderNotFound_OrderNotFoundExceptionThrown() {
        final Long userId = 1L;
        final Long orderId = 1L;

        User user = new User();
        user.setId(userId);

        given(userRepository.findById(userId))
                .willReturn(user);
        given(orderRepository.findByUser(user, orderId))
                .willReturn(null);

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.findByUserId(userId, orderId);
        });

        then(userRepository)
                .should(only())
                .findById(anyLong());

        then(orderRepository)
                .should(only())
                .findByUser(any(User.class), anyLong());

        then(orderConverter)
                .should(never())
                .entityToDto(any(Order.class));
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
                .willReturn(user);
        given(orderConverter.dtoToEntity(orderDtoToCreate))
                .willReturn(orderToCreate);
        given(orderRepository.save(orderToCreate))
                .willReturn(savedOrder);
        given(orderConverter.entityToDto(savedOrder))
                .willReturn(savedOrderDto);

        orderService.create(userId, orderDtoToCreate);

        then(userRepository)
                .should(only())
                .findById(anyLong());

        then(orderConverter)
                .should(times(1))
                .dtoToEntity(any(OrderDto.class));

        then(orderRepository)
                .should(only())
                .save(any(Order.class));

        then(orderConverter)
                .should(times(1))
                .entityToDto(any(Order.class));
    }

    @Test
    void create_UserNotFound_UserNotFoundExceptionThrown() {
        final Long userId = 1L;

        OrderDto orderDtoToCreate = new OrderDto();

        given(userRepository.findById(userId))
                .willReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            orderService.create(userId, orderDtoToCreate);
        });

        then(userRepository)
                .should(only())
                .findById(anyLong());

        then(orderConverter)
                .should(never())
                .dtoToEntity(any(OrderDto.class));

        then(orderRepository)
                .should(never())
                .save(any(Order.class));

        then(orderConverter)
                .should(never())
                .entityToDto(any(Order.class));
    }
}