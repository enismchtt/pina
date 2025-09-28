package com.bbm.backend.repositories;

import com.bbm.backend.models.*;
import com.bbm.backend.utils.enums.ReviewType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCustomer(Customer customer);
    List<Review> findByRestaurant(Restaurant restaurant);
    List<Review> findByCourier(Courier courier);
    List<Review> findByRatingGreaterThanEqual(int rating);
    List<Review> findByReviewType(ReviewType reviewType);
}