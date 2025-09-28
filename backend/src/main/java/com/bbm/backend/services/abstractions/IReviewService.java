package com.bbm.backend.services.abstractions;

import com.bbm.backend.models.Review;
import com.bbm.backend.utils.enums.ReviewType;

import java.util.List;
import java.util.Optional;

public interface IReviewService {
    List<Review> findAll();
    Optional<Review> findById(Long id);
    Review save(Review review);
    void deleteById(Long id);
    List<Review> findByCustomerId(Long customerId);
    List<Review> findByRestaurantId(Long restaurantId);
    List<Review> findByCourierId(Long courierId);
    List<Review> findByRatingGreaterThanEqual(int rating);
    List<Review> findByReviewType(ReviewType reviewType);
    void addReview(Review review);
}
