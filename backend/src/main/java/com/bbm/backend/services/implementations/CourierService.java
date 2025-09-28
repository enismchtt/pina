package com.bbm.backend.services.implementations;

import com.bbm.backend.models.Courier;
import com.bbm.backend.models.Customer;
import com.bbm.backend.models.Order;
import com.bbm.backend.models.Restaurant;
import com.bbm.backend.repositories.CourierRepository;
import com.bbm.backend.repositories.OrderRepository;
import com.bbm.backend.services.abstractions.ICourierService;
import com.bbm.backend.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourierService implements ICourierService {

	private final CourierRepository courierRepository;
	private final OrderRepository orderRepository;

	@Override
	public List<Courier> findAll() {
		return courierRepository.findAll();
	}

	@Override
	public Optional<Courier> findById(Long id) {
		return courierRepository.findById(id);
	}

	@Override
	public Courier save(Courier courier) {
		return courierRepository.save(courier);
	}

	@Override
	public void deleteById(Long id) {
		courierRepository.deleteById(id);
	}

	@Override
	public boolean existsByUsername(String username) {
		return courierRepository.existsByUsername(username);
	}

	@Override
	public List<Courier> findByAvailableTrue() {
		return courierRepository.findByAvailableTrue();
	}

	@Override
	public List<Courier> findByApprovedTrue() {
		return courierRepository.findByApprovedTrue();
	}

	@Override
	public List<Courier> findByRatingGreaterThanEqual(double rating) {
		return courierRepository.findByRatingGreaterThanEqual(rating);
	}

	@Override
	public void setAvailability(Long courierId, boolean available) {
		courierRepository.findById(courierId).ifPresent(courier -> {
			courier.setAvailable(available);
			courierRepository.save(courier);
		});
	}

	@Override
	public void approveCourier(Long courierId) {
		courierRepository.findById(courierId).ifPresent(courier -> {
			courier.setApproved(true);
			courierRepository.save(courier);
		});
	}

	@Override
	public void rejectCourier(Long courierId) {
		courierRepository.findById(courierId).ifPresent(courier -> {
			courier.setApproved(false);
			courierRepository.save(courier);
		});
	}

	@Override
	public List<Order> getDeliveries(Long courierId) {
		return orderRepository.findByCourierId(courierId);
	}

	@Override
	public Set<Restaurant> getRegisteredRestaurants(Long courierId) {
		return courierRepository.findById(courierId)
				.map(Courier::getRestaurants)
				.orElse(Set.of());
	}

	@Override
	public Optional<Courier> findByUsername(String username) {
		return courierRepository.findByUsername(username);
	}

	@Override
	public Optional<Courier> findByEmail(String email) {
		return courierRepository.findByEmail(email);
	}

	@Override
	public boolean existsByEmail(String email) {
		return courierRepository.existsByEmail(email);
	}
}
