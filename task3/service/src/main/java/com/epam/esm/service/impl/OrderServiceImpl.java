package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.criteria.Criteria;
import com.epam.esm.repository.criteria.CriteriaSearch;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.util.ParamsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.HashSet;
import java.util.stream.Collectors;

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
    public List<OrderDto> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orderConverter.entityToDtoList(orders);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderDto findById(Long id) {
        Order order = orderRepository.findById(id);
        return orderConverter.entityToDto(order);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderDto> findByUserId(Long id) {
        List<Order> orders = orderRepository.findByUserId(id);
        return orderConverter.entityToDtoList(orders);
    }

    @Transactional
    @Override
    public OrderDto placeOrder(Long userId, OrderDto orderDto) {
        Order order = orderConverter.dtoToEntity(orderDto);

        User user = userRepository.findById(userId);

        List<GiftCertificate> gifts = order.getGiftCertificates().stream()
                .map(giftCertificate -> {
                    Long giftId = giftCertificate.getId();
                    Criteria criteria = ParamsUtil.buildCriteria(CriteriaSearch.ID, String.valueOf(giftId));
                    return giftCertificateRepository.find(criteria);
                }).collect(Collectors.toList());

        BigDecimal amount = gifts.stream()
                .map(GiftCertificate::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setCreateDate(Instant.now().truncatedTo(ChronoUnit.MICROS));
        order.setUser(user);
        order.setAmount(amount);
        order.setGiftCertificates(new HashSet<>(gifts));

        Order savedOrder = orderRepository.saveOrder(order);
        return orderConverter.entityToDto(savedOrder);
    }
}
