package com.example.privatepr.services.impl;

import lombok.RequiredArgsConstructor;
import com.example.privatepr.dto.ReviewDto;
import com.example.privatepr.models.Client;
import com.example.privatepr.models.Hotel;
import com.example.privatepr.models.Review;
import com.example.privatepr.repositories.ReviewRepository;
import com.example.privatepr.services.ReviewService;
import com.example.privatepr.utils.VerifyingAccess;
import com.example.privatepr.utils.erorsHandler.ErrorHandler;
import com.example.privatepr.utils.exeptions.ClientErrorException;
import com.example.privatepr.utils.exeptions.HotelErrorException;
import com.example.privatepr.utils.exeptions.ReviewErrorException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final HotelServiceImpl hotelService;
    private final ClientServiceImpl clientService;
    private final ModelMapper modelMapper;
    private final VerifyingAccess verifyingAccess;
    private final ErrorHandler errorHandler;

    @Transactional
    public void save(Review review) {
        verifyingAccess.checkPossibilityAction(review.getClient().getLogin());
        review.setDate(LocalDate.now());
        reviewRepository.save(review);
    }

    @Transactional
    public void delete(int id) {
        Review reviewInDB = getReview(id).orElseThrow(() -> new ReviewErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.review.exception.review-not-found-by-id")
                .formatted(id)));

        verifyingAccess.checkPossibilityAction(reviewInDB.getClient().getLogin());
        reviewRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Review reviewByUpdate) {
        Review reviewInDB = getReview(id).orElseThrow(() -> new ReviewErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.review.exception.review-not-found-by-id")
                .formatted(id)));

        verifyingAccess.checkPossibilityAction(reviewByUpdate.getClient().getLogin(), reviewInDB.getClient().getLogin());
        reviewByUpdate.setId(id);
        reviewByUpdate.setDate(LocalDate.now());
        reviewRepository.save(reviewByUpdate);
    }

    @Transactional(readOnly = true)
    public Optional<Review> getReview(int id) {
        return reviewRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Review> findAllByHotelId(int hotelId) {
        return reviewRepository.findAllByHotelId(hotelId);
    }

    @Transactional(readOnly = true)
    public List<Review> findAllByClientId(int clientId) {
        return reviewRepository.findAllByClientId(clientId);
    }

    public Review getValidReview(ReviewDto reviewDto) {
        Hotel hotel = hotelService.getHotel(reviewDto.getHotelId()).orElseThrow(() -> new HotelErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.hotel.exception.hotel-not-found")));

        String login = reviewDto.getClientLogin();
        Client client = clientService.findByLogin(login).orElseThrow(() -> new ClientErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.client.exception.requested-login-not-found")
                .formatted(login)));

        Review review = convertToReview(reviewDto);
        review.setHotel(hotel);
        review.setClient(client);

        return review;
    }

    private Review convertToReview(ReviewDto reviewDto) {
        return modelMapper.map(reviewDto, Review.class);
    }
}
