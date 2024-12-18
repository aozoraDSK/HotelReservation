package com.example.privatepr.services.impl;

import lombok.RequiredArgsConstructor;
import com.example.privatepr.models.Hotel;
import com.example.privatepr.repositories.HotelRepository;
import com.example.privatepr.services.HotelService;
import com.example.privatepr.utils.erorsHandler.ErrorHandler;
import com.example.privatepr.utils.exeptions.HotelErrorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final ErrorHandler errorHandler;

    @Transactional
    public void save(Hotel hotel) {
        hotel.getRoomList().forEach(room -> room.setHotel(hotel));
        hotelRepository.save(hotel);
    }

    @Transactional
    public void delete(int id) {
        if (getHotel(id).isPresent()) {
            hotelRepository.deleteById(id);
        } else {
            throw new HotelErrorException(errorHandler
                    .getErrorMessage("validation.hotelBook.hotel.exception.hotel-not-found"));
        }
    }

    @Transactional
    public void update(int id, Hotel hotelByUpdate) {
        hotelByUpdate.setId(id);
        hotelRepository.save(hotelByUpdate);
    }

    @Transactional(readOnly = true)
    public Optional<Hotel> getHotel(int id) {
        return hotelRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Hotel> findByName(String name) {
        return hotelRepository.findByName(name);
    }
}
