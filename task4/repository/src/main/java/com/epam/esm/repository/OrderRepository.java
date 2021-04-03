package com.epam.esm.repository;

import com.epam.esm.domain.entity.Order;
import com.epam.esm.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUser(User user, Pageable page);
    Order findByIdAndUser(Long orderId, User user);
}
