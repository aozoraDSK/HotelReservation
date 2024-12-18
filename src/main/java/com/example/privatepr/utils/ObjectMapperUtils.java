package com.example.privatepr.utils;

import com.example.privatepr.dto.BookDto;
import com.example.privatepr.models.Book;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperUtils {

    @Bean
    public ModelMapper modelMapper () {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(BookDto.class, Book.class).addMappings(mapper -> mapper.skip(Book::setId));
        return modelMapper;
    }
}
