package com.bbm.backend.controllers;

import com.bbm.backend.dto.CourierDTO;
import com.bbm.backend.dto.OrderDTO;
import com.bbm.backend.dto.RestaurantDTO;
import com.bbm.backend.models.Courier;
import com.bbm.backend.models.Order;
import com.bbm.backend.models.Restaurant;
import com.bbm.backend.services.implementations.CourierService;
import com.bbm.backend.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/couriers")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class CourierController {

	private final CourierService courierService;

	@GetMapping
	public ResponseEntity<List<CourierDTO>> getAllCouriers() {
		return ResponseEntity.ok(
				courierService.findAll().stream().map(this::toDTO).collect(Collectors.toList())
		);
	}

	@GetMapping("/available")
	public ResponseEntity<List<CourierDTO>> getAvailableCouriers() {
		return ResponseEntity.ok(
				courierService.findByAvailableTrue().stream().map(this::toDTO).collect(Collectors.toList())
		);
	}

	@GetMapping("/approved")
	public ResponseEntity<List<CourierDTO>> getApprovedCouriers() {
		return ResponseEntity.ok(
				courierService.findByApprovedTrue().stream().map(this::toDTO).collect(Collectors.toList())
		);
	}

	@GetMapping("/rating")
	public ResponseEntity<List<CourierDTO>> getCouriersByRating(@RequestParam double minRating) {
		return ResponseEntity.ok(
				courierService.findByRatingGreaterThanEqual(minRating).stream().map(this::toDTO).collect(Collectors.toList())
		);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CourierDTO> getCourier(@PathVariable Long id) {
		return courierService.findById(id)
				.map(courier -> ResponseEntity.ok(toDTO(courier)))
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/username")
	public ResponseEntity<CourierDTO> getCourierByUsername(@RequestParam String username) {
		return courierService.findByUsername(username).map(courier -> ResponseEntity.ok(toDTO(courier)))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<CourierDTO> createCourier(@RequestBody CourierDTO dto) {
		if (courierService.existsByUsername(dto.getUsername())) {
			return ResponseEntity.badRequest().build();
		}
		dto.setType("courier");
		Courier courier = toEntity(dto);
		courier.setPassword(PasswordUtil.encode(courier.getPassword()));
		Courier saved = courierService.save(courier);
		return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CourierDTO> updateCourier(@PathVariable Long id, @RequestBody CourierDTO dto) {
		if (courierService.findById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Courier courier = toEntity(dto);
		courier.setId(id);
		courier.setPassword(PasswordUtil.encode(courier.getPassword()));
		return ResponseEntity.ok(toDTO(courierService.save(courier)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCourier(@PathVariable Long id) {
		if (courierService.findById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		courierService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/approve")
	public ResponseEntity<Void> approveCourier(@PathVariable Long id) {
		courierService.approveCourier(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/reject")
	public ResponseEntity<Void> rejectCourier(@PathVariable Long id) {
		courierService.rejectCourier(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/availability")
	public ResponseEntity<Void> setAvailability(@PathVariable Long id, @RequestBody boolean available) {
		courierService.setAvailability(id, available);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/deliveries")
	public ResponseEntity<List<OrderDTO>> getDeliveries(@PathVariable Long id) {
		return ResponseEntity.ok(courierService.getDeliveries(id).stream().map(this::toOrderDTO).collect(Collectors.toList()));
	}

	@GetMapping("/{id}/restaurants")
	public ResponseEntity<Set<RestaurantDTO>> getRegisteredRestaurants(@PathVariable Long id) {
		return ResponseEntity.ok(courierService.getRegisteredRestaurants(id).stream()
				.map(this::toRestaurantDTO).collect(Collectors.toSet()));
	}

	private CourierDTO toDTO(Courier c) {
		CourierDTO dto = new CourierDTO();
		dto.setId(c.getId());
		dto.setUsername(c.getUsername());
		dto.setPassword(c.getPassword());
		dto.setEmail(c.getEmail());
		dto.setPhoneNumber(c.getPhoneNumber());
		dto.setAddress(c.getAddress());
		dto.setFirstName(c.getFirstName());
		dto.setLastName(c.getLastName());
		dto.setVehicleInfo(c.getVehicleInfo());
		dto.setAvailable(c.isAvailable());
		dto.setApproved(c.isApproved());
		dto.setRating(c.getRating());
		dto.setType(c.getType());
		dto.setBanned(c.isBanned());
		return dto;
	}

	private Courier toEntity(CourierDTO dto) {
		Courier c = new Courier();
		c.setId(dto.getId());
		c.setUsername(dto.getUsername());
		c.setPassword(dto.getPassword());
		c.setEmail(dto.getEmail());
		c.setPhoneNumber(dto.getPhoneNumber());
		c.setAddress(dto.getAddress());
		c.setFirstName(dto.getFirstName());
		c.setLastName(dto.getLastName());
		c.setVehicleInfo(dto.getVehicleInfo());
		c.setAvailable(dto.isAvailable());
		c.setApproved(dto.isApproved());
		c.setRating(dto.getRating());
		c.setType(dto.getType());
		return c;
	}
	private OrderDTO toOrderDTO(Order order) {
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

		dto.setCustomerName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
		dto.setRestaurantName(order.getRestaurant().getName());

		if (order.getCourier() != null) {
			dto.setCourierId(order.getCourier().getId());
		}

		return dto;
	}
	private RestaurantDTO toRestaurantDTO(Restaurant restaurant) {
		RestaurantDTO dto = new RestaurantDTO();
		dto.setId(restaurant.getId());
		dto.setUsername(restaurant.getUsername());
		dto.setPassword(restaurant.getPassword());
		dto.setEmail(restaurant.getEmail());
		dto.setPhoneNumber(restaurant.getPhoneNumber());
		dto.setActive(restaurant.isActive());
		dto.setName(restaurant.getName());
		dto.setDescription(restaurant.getDescription());
		dto.setAddress(restaurant.getAddress());
		dto.setCuisineType(restaurant.getCuisineType());
		dto.setRating(restaurant.getRating());
		dto.setBusinessHours(restaurant.getBusinessHours());
		dto.setApproved(restaurant.isApproved());
		dto.setDeliveryRange(restaurant.getDeliveryRange());
		dto.setType(restaurant.getType());
		return dto;
	}
}
