package com.bbm.backend.services.abstractions;

import com.bbm.backend.models.Order;
import com.bbm.backend.models.OrderItem;
import com.bbm.backend.utils.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<Order> findAll();
    Optional<Order> findById(Long id);
    Order save(Order order);
    void deleteById(Long id);
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByRestaurantId(Long restaurantId);
    List<Order> findByCourierId(Long courierId);
    List<Order> findByOrderStatus(OrderStatus status);
    List<Order> findByOrderTimeBetween(LocalDateTime start, LocalDateTime end);
    void updateOrderStatus(Long orderId, OrderStatus status);
    void setOrderNote(Long orderId, String note);
    void assignCourier(Long orderId, Long courierId);
    void unassignCourier(Long orderId);
    List<OrderItem> getOrderItems(Long orderId);
    Order placeOrder(Long customerId, Long restaurantId, List<OrderItem> items, String deliveryAddress);
}
