package com.bbm.backend.services.implementations;

import com.bbm.backend.models.*;
import com.bbm.backend.repositories.ReviewRepository;
import com.bbm.backend.services.abstractions.IReviewService;
import com.bbm.backend.utils.enums.ReviewType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public List<Review> findByCustomerId(Long customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);
        return reviewRepository.findByCustomer(customer);
    }

    @Override
    public List<Review> findByRestaurantId(Long restaurantId) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        return reviewRepository.findByRestaurant(restaurant);
    }

    @Override
    public List<Review> findByCourierId(Long courierId) {
        Courier courier = new Courier();
        courier.setId(courierId);
        return reviewRepository.findByCourier(courier);
    }

    @Override
    public List<Review> findByRatingGreaterThanEqual(int rating) {
        return reviewRepository.findByRatingGreaterThanEqual(rating);
    }

    @Override
    public List<Review> findByReviewType(ReviewType reviewType) {
        return reviewRepository.findByReviewType(reviewType);
    }

    @Override
    public void addReview(Review review) {
        reviewRepository.save(review);
    }
}
