package com.epam.esm.repository.impl;

import com.epam.esm.domain.entity.GiftCertificate;
import com.epam.esm.domain.entity.Order;
import com.epam.esm.domain.entity.OrderGiftCertificate;
import com.epam.esm.domain.entity.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.config.RepositoryTestConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = RepositoryTestConfig.class)
@Transactional
class OrderRepositoryImplTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    private static int offset;
    private static int limit;

    private List<Order> orderList;

    @BeforeAll
    static void beforeAll() {
        offset = 0;
        limit = 5;
    }

    @BeforeEach
    void beforeEach() {
        orderList = orderRepository.findAll(offset, limit);
        assertNotEquals(0, orderList.size());
    }

    @Test
    void findAll_SearchForOrdersWithOffsetAndLimit_ReturnOrderList() {
        List<Order> orderList = orderRepository.findAll(offset, limit);
        assertEquals(orderList, orderList);
    }

    @Test
    void findById_SearchForOrderById_ReturnFoundOrder() {
        Order existedOrder = orderList.get(0);
        final Long existedOrderId = existedOrder.getId();

        Order actual = orderRepository.findById(existedOrderId);

        assertEquals(existedOrder, actual);
    }

    @Test
    void findAllByUser_SearchForOrdersByUserWithOffsetAndLimit_ReturnFoundOrderList() {
        User user = userRepository.findById(1L);

        List<Order> actual = orderRepository.findAllByUser(user, offset, limit);

        List<Order> expected = orderList.stream()
                .filter(order -> order.getUser().equals(user))
                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    void findByUser_SearchForOrderByUserAndOrderId_ReturnFoundOrder() {
        final Long userId = 1L;
        final Long orderId = 1L;

        User user = userRepository.findById(userId);
        Order order = orderRepository.findByUser(user, orderId);

        assertNotNull(order);
    }

    @Test
    void save_SavingOrder_ReturnSavedOrder() {
        Order orderToPersist = new Order();
        User user = userRepository.findById(1L);
        GiftCertificate giftCertificate = giftCertificateRepository.findById(1L);

        OrderGiftCertificate orderGiftCertificate = new OrderGiftCertificate();
        orderGiftCertificate.setGiftCertificate(giftCertificate);
        orderGiftCertificate.setQuantity(2);
        orderGiftCertificate.setOrder(orderToPersist);

        orderToPersist.setOrderGiftCertificates(Set.of(orderGiftCertificate));
        orderToPersist.setAmount(giftCertificate.getPrice()
                .multiply(new BigDecimal(orderGiftCertificate.getQuantity())));
        orderToPersist.setCreateDate(Instant.now().truncatedTo(ChronoUnit.MICROS));
        orderToPersist.setUser(user);

        Order savedOrder = orderRepository.save(orderToPersist);

        assertNotNull(savedOrder.getId());
    }
}
