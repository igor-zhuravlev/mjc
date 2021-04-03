package com.epam.esm.service.impl;

import com.epam.esm.domain.entity.GiftCertificate;
import com.epam.esm.domain.entity.Order;
import com.epam.esm.domain.entity.OrderGiftCertificate;
import com.epam.esm.domain.entity.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.constant.ServiceError;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.exception.order.OrderNotFoundException;
import com.epam.esm.service.exception.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private GiftCertificateRepository giftCertificateRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Converter<Order, OrderDto> orderConverter;

    @Transactional(readOnly = true)
    @Override
    public Page<OrderDto> findAll(PageDto pageDto) {
        Page<Order> orderPage = orderRepository.findAll(PageRequest.of(pageDto.getPage(), pageDto.getSize()));
        return orderPage.map(orderConverter::entityToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderDto findById(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        Order order = orderOptional.orElseThrow(() ->
                new OrderNotFoundException(ServiceError.ORDER_NOT_FOUND.getCode()));
        return orderConverter.entityToDto(order);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderDto> findAllByUserId(Long userId, PageDto pageDto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() ->
                new UserNotFoundException(ServiceError.USER_NOT_FOUND.getCode()));
        Page<Order> orderPage = orderRepository.findAllByUser(user, PageRequest.of(pageDto.getPage(), pageDto.getSize()));
        return orderPage.map(orderConverter::entityToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderDto findByUserId(Long userId, Long orderId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() ->
                new UserNotFoundException(ServiceError.USER_NOT_FOUND.getCode()));
        Order order = orderRepository.findByIdAndUser(orderId, user);
        if (order == null) {
            throw new OrderNotFoundException(ServiceError.ORDER_NOT_FOUND.getCode());
        }
        return orderConverter.entityToDto(order);
    }

    @Transactional
    @Override
    public OrderDto create(Long userId, OrderDto orderDto) {
        Optional<User> optionalUser = userRepository.findById(userId);

        User user = optionalUser.orElseThrow(() ->
                new UserNotFoundException(ServiceError.USER_NOT_FOUND.getCode()));

        Order order = orderConverter.dtoToEntity(orderDto);

        for (OrderGiftCertificate orderGiftCertificate : order.getOrderGiftCertificates()) {
            Long giftCertificateId = orderGiftCertificate.getGiftCertificate().getId();
            Optional<GiftCertificate> foundGiftCertificate =
                    giftCertificateRepository.findById(giftCertificateId);
            orderGiftCertificate.setGiftCertificate(foundGiftCertificate.get());
            orderGiftCertificate.setOrder(order);
        }

        BigDecimal amount = order.getOrderGiftCertificates().stream()
                .map(orderGiftCertificate -> {
                    BigDecimal quantity = new BigDecimal(orderGiftCertificate.getQuantity());
                    BigDecimal price = orderGiftCertificate.getGiftCertificate().getPrice();
                    return quantity.multiply(price);
                }).reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setCreateDate(Instant.now().truncatedTo(ChronoUnit.MICROS));
        order.setUser(user);
        order.setAmount(amount);

        order = orderRepository.save(order);
        return orderConverter.entityToDto(order);
    }
}
