package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Order_;
import com.epam.esm.entity.User;
import com.epam.esm.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Order> findAll(int offset, int limit) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);

        criteriaQuery.select(root);

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Order findById(Long id) {
        return entityManager.find(Order.class, id);
    }

    @Override
    public void delete(Order order) {
        entityManager.remove(order);
    }

    @Override
    public List<Order> findAllByUser(User user, int offset, int limit) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);

        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get(Order_.user), user));

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Order findByUser(User user, Long orderId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);

        Predicate predicateOrder = criteriaBuilder.equal(root.get(Order_.id), orderId);
        Predicate predicateUser = criteriaBuilder.equal(root.get(Order_.user), user);

        criteriaQuery.select(root)
                .where(criteriaBuilder.and(predicateOrder, predicateUser));

        return entityManager.createQuery(criteriaQuery).getResultStream()
                .findFirst().orElse(null);
    }

    @Override
    public Order save(Order order) {
        entityManager.persist(order);
        return order;
    }
}
