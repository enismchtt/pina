package com.bbm.backend.repositories;

import com.bbm.backend.models.MenuItem;
import com.bbm.backend.models.Order;
import com.bbm.backend.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
    List<OrderItem> findByMenuItem(MenuItem menuItem);
}
