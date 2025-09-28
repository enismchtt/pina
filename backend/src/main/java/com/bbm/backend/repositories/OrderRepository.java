package com.bbm.backend.repositories;

import com.bbm.backend.models.*;
import com.bbm.backend.utils.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
    List<Order> findByRestaurant(Restaurant restaurant);
    List<Order> findByCourier(Courier courier);
    List<Order> findByCourierId(Long courierId);
    List<Order> findByOrderStatus(OrderStatus status);
    List<Order> findByOrderTimeBetween(LocalDateTime start, LocalDateTime end);
}