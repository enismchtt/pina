package com.bbm.backend.services.implementations;

import com.bbm.backend.exceptions.ResourceNotFoundException;
import com.bbm.backend.models.*;
import com.bbm.backend.repositories.*;
import com.bbm.backend.services.abstractions.IOrderService;
import com.bbm.backend.utils.enums.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final OrderItemRepository orderItemRepository;
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final RestaurantRepository restaurantRepository;
    @Autowired
    private final CourierRepository courierRepository;


    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        return orderRepository.findByCustomer(customer);
    }

    @Override
    public List<Order> findByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        return orderRepository.findByRestaurant(restaurant);
    }

    @Override
    public List<Order> findByCourierId(Long courierId) {
        return orderRepository.findByCourierId(courierId);
    }

    @Override
    public List<Order> findByOrderStatus(OrderStatus status) {
        return orderRepository.findByOrderStatus(status);
    }

    @Override
    public List<Order> findByOrderTimeBetween(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findByOrderTimeBetween(start, end);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        order.setOrderStatus(status);

        // If status is DELIVERED, set the delivery time
        if (status == OrderStatus.DELIVERED) {
            order.setDeliveryTime(LocalDateTime.now());
        }

        orderRepository.save(order);
    }
    @Override
    @Transactional
    public void setOrderNote(Long orderId, String note) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        order.setSpecialInstructions(note);

        orderRepository.save(order);
    }


    @Override
    @Transactional
    public void assignCourier(Long orderId, Long courierId) {
        //TODO: We should implement the accepting logic here
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new ResourceNotFoundException("Courier not found with id: " + courierId));

        // Check if courier is available
        if (!courier.isAvailable()) {
            throw new IllegalStateException("Courier is not available");
        }


//        if (!courier.getRestaurants().contains(order.getRestaurant())) {
//            throw new IllegalStateException("Courier is not registered with the restaurant");
//        }

        order.setCourier(courier);
        List<Order> deliveries = courier.getDeliveries();
        deliveries.add(order);
        courier.setDeliveries(deliveries);
        courierRepository.save(courier);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void unassignCourier(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (order.getCourier() == null) {
            throw new IllegalStateException("No courier assigned to this order");
        }

        Courier courier = order.getCourier();

        List<Order> deliveries = courier.getDeliveries();
        deliveries.remove(order);
        courier.setDeliveries(deliveries);

        order.setCourier(null);

        courierRepository.save(courier);
        orderRepository.save(order);
    }


    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        return orderItemRepository.findByOrder(order);
    }

    @Override
    @Transactional
    public Order placeOrder(Long customerId, Long restaurantId, List<OrderItem> items, String deliveryAddress) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        Order order = new Order();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setOrderTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setDeliveryAddress(deliveryAddress);

        // Calculate total amount
        double totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        order.setTotalAmount(totalAmount);

        // Save order first to get the ID
        Order savedOrder = orderRepository.save(order);

        // Set the order reference for each item and save
        items.forEach(item -> {
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        });

        return savedOrder;
    }
}
