package com.example.privatepr.repositories;

import com.example.privatepr.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findAllByHotelId(int hotelId);
    List<Review> findAllByClientId(int clientId);
}
