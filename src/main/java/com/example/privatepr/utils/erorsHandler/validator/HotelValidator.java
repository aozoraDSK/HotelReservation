package com.example.privatepr.utils.erorsHandler.validator;

import lombok.AllArgsConstructor;
import com.example.privatepr.models.Hotel;
import com.example.privatepr.services.impl.HotelServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Locale;

@Component
@AllArgsConstructor
public class HotelValidator implements Validator {
    private final HotelServiceImpl hotelService;
    private final MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return Hotel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Hotel hotel = (Hotel) target;
        List<Hotel> listHotelWithSameName = hotelService.findByName(hotel.getName());

        if (!listHotelWithSameName.isEmpty()) {
            for (Hotel hotelWithSameName : listHotelWithSameName) {
                if (hotelWithSameName.equals(hotel)) {
                    //Значит объект уже существует и мы производим обновление
                    return;
                } else if (hotelWithSameName.getAddress().equals(hotel.getAddress())) {
                    errors.rejectValue("address", "409", messageSource
                            .getMessage("validation.hotelBook.hotel.hotel-name.hotel-exist", null, Locale.getDefault()));
                    return;
                }
            }
        }
    }
}
