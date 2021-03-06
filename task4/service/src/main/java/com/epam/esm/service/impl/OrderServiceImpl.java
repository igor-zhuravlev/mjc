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
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    @Override
    public Page<OrderDto> findAll(Pageable page) {
        Page<Order> orderPage = orderRepository.findAll(page);
        return orderPage.map(order -> modelMapper.map(order, OrderDto.class));
    }

    @Transactional(readOnly = true)
    @Override
    public OrderDto findById(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        Order order = orderOptional.orElseThrow(() ->
                new ResourceNotFoundException(ServiceError.ORDER_NOT_FOUND.getCode()));
        return modelMapper.map(order, OrderDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderDto> findAllByUserId(Long userId, Pageable page) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() ->
                new ResourceNotFoundException(ServiceError.USER_NOT_FOUND.getCode()));
        Page<Order> orderPage = orderRepository.findAllByUser(user, page);
        return orderPage.map(order -> modelMapper.map(order, OrderDto.class));
    }

    @Transactional(readOnly = true)
    @Override
    public OrderDto findByUserId(Long userId, Long orderId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() ->
                new ResourceNotFoundException(ServiceError.USER_NOT_FOUND.getCode()));
        Order order = orderRepository.findByIdAndUser(orderId, user);
        if (order == null) {
            throw new ResourceNotFoundException(ServiceError.ORDER_NOT_FOUND.getCode());
        }
        return modelMapper.map(order, OrderDto.class);
    }

    @Transactional
    @Override
    public OrderDto create(Long userId, OrderDto orderDto) {
        Optional<User> optionalUser = userRepository.findById(userId);

        User user = optionalUser.orElseThrow(() ->
                new ResourceNotFoundException(ServiceError.USER_NOT_FOUND.getCode()));

        Order order = modelMapper.map(orderDto, Order.class);

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
        return modelMapper.map(order, OrderDto.class);
    }
}
