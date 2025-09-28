package com.bbm.backend.dto;

import com.bbm.backend.utils.enums.ReviewType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;
    private int rating;
    private String comment;
    private LocalDateTime reviewTime;
    private ReviewType reviewType;
    private Long customerId;
    private Long restaurantId;
    private Long courierId;
}