package com.bbm.backend.controllers;

import com.bbm.backend.dto.OrderDTO;
import com.bbm.backend.dto.OrderItemDTO;
import com.bbm.backend.models.*;
import com.bbm.backend.services.implementations.OrderService;
import com.bbm.backend.services.implementations.RestaurantService;
import com.bbm.backend.utils.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000") // Add this line
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private final OrderService orderService;
    @Autowired
    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomer(@PathVariable Long customerId) {
        List<OrderDTO> orders = orderService.findByCustomerId(customerId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByRestaurant(@PathVariable Long restaurantId) {
        List<OrderDTO> orders = orderService.findByRestaurantId(restaurantId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/courier/{courierId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCourier(@PathVariable Long courierId) {
        List<OrderDTO> orders = orderService.findByCourierId(courierId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderDTO> orders = orderService.findByOrderStatus(status).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/timerange")
    public ResponseEntity<List<OrderDTO>> getOrdersByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<OrderDTO> orders = orderService.findByOrderTimeBetween(start, end).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> placeOrder(
            @RequestParam Long customerId,
            @RequestParam Long restaurantId,
            @RequestParam String deliveryAddress,
            @RequestBody List<OrderItemDTO> orderItems) {

        List<OrderItem> items = orderItems.stream()
                .map(this::convertToOrderItemEntity)
                .collect(Collectors.toList());


        Order order = orderService.placeOrder(customerId, restaurantId, items, deliveryAddress);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(order));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        if (!orderService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        orderService.updateOrderStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/note")
    public ResponseEntity<Void> setOrderNote(@RequestParam Long id, @RequestParam String note) {
        if (!orderService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        orderService.setOrderNote(id, note);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/assign-courier/{courierId}")
    public ResponseEntity<Void> assignCourier(@PathVariable Long id, @PathVariable Long courierId) {
        if (!orderService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        orderService.assignCourier(id, courierId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/unassign-courier")
    public ResponseEntity<Void> unassignCourier(@PathVariable Long id) {
        if (!orderService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        orderService.unassignCourier(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}/items")
    public ResponseEntity<List<OrderItemDTO>> getOrderItems(@PathVariable Long id) {
        if (!orderService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<OrderItemDTO> items = orderService.getOrderItems(id).stream()
                .map(this::convertToOrderItemDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }

    private OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderTime(order.getOrderTime());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setDeliveryTime(order.getDeliveryTime());
        dto.setSpecialInstructions(order.getSpecialInstructions());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setRestaurantId(order.getRestaurant().getId());

        dto.setCustomerName(order.getCustomer().getFirstName());
//        dto.setCustomerName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
        dto.setRestaurantName(order.getRestaurant().getName());

        if (order.getCourier() != null) {
            dto.setCourierId(order.getCourier().getId());
        }

        return dto;
    }

    private OrderItemDTO convertToOrderItemDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        dto.setSpecialInstructions(orderItem.getSpecialInstructions());
        dto.setMenuItemId(orderItem.getMenuItem().getId());
        dto.setMenuItemName(orderItem.getMenuItem().getName());
        return dto;
    }

    private OrderItem convertToOrderItemEntity(OrderItemDTO dto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(dto.getId());
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPrice(dto.getPrice());
        orderItem.setSpecialInstructions(dto.getSpecialInstructions());

//        Optional<MenuItem> menuItem = restaurantService.getMenuItem(dto.getMenuItemId());
//        menuItem.ifPresent(orderItem::setMenuItem);
        return orderItem;
    }
}