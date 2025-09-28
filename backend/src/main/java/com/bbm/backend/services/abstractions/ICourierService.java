package com.bbm.backend.services.abstractions;

import com.bbm.backend.models.Courier;
import com.bbm.backend.models.Order;
import com.bbm.backend.models.Restaurant;

import java.util.List;
import java.util.Set;

public interface ICourierService extends IUserService<Courier> {
    List<Courier> findByAvailableTrue();
    List<Courier> findByApprovedTrue();
    List<Courier> findByRatingGreaterThanEqual(double rating);
    void setAvailability(Long courierId, boolean available);
    void approveCourier(Long courierId);
    void rejectCourier(Long courierId);
    List<Order> getDeliveries(Long courierId);
    Set<Restaurant> getRegisteredRestaurants(Long courierId);
}
