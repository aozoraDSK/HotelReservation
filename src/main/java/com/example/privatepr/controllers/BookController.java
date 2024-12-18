package com.example.privatepr.controllers;

import com.example.privatepr.utils.exeptions.BookErrorException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.example.privatepr.dto.BookDto;
import com.example.privatepr.models.Book;
import com.example.privatepr.services.impl.BookServiceImpl;
import com.example.privatepr.utils.erorsHandler.ErrorHandler;
import com.example.privatepr.utils.exeptions.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookServiceImpl bookService;
    private final ModelMapper modelMapper;
    private final ErrorHandler errorHandler;

    @GetMapping("/{id_book}")
    public String getBook(@PathVariable("id_book") int id, Model model) {
        BookDto bookDto = convertToBookDto(bookService.getBook(id).orElseThrow(() -> new BookErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.book.exception.book-not-found"))));
        model.addAttribute("book", bookDto);
        return "book/book";
    }

    @GetMapping("/{id_room}/allRoomBooks")
    public String getAllRoomBooks(@PathVariable("id_room") long roomId, Model model) {
        List<BookDto> bookDtoList = convertToBookDtoList(bookService.findAllByRoomId(roomId));
        model.addAttribute("bookList", bookDtoList);
        model.addAttribute("roomId", roomId);
        return "book/allRoomBooks";
    }

    @GetMapping("/{hotel_id}/allBooksInHotel")
    public String getAllBookInHotel(@PathVariable("hotel_id") int hotelId, Model model) {
        List<BookDto> bookDtoList = convertToBookDtoList(bookService.findAllBookInHotel(hotelId));
        model.addAttribute("bookList", bookDtoList);
        return "book/allHotelBooks";
    }

    @GetMapping("/all")
    public String getAllBooks(Model model) {
        List<BookDto> bookDtoList = convertToBookDtoList(bookService.getAllBook());
        model.addAttribute("bookList", bookDtoList);
        return "book/book-all";
    }

    @GetMapping("/{client_login}/allClientBooks")
    public String getAllBookByClient(@PathVariable("client_login") String login, Model model) {
        List<BookDto> bookDtoList = convertToBookDtoList(bookService.findAllByClientId(login));
        model.addAttribute("bookList", bookDtoList);
        return "book/allClientsBooks";
    }

    @GetMapping("/{id_room}/new")
    public String getNewBookPage(@PathVariable("id_room") int roomId, Model model) {
        BookDto bookDto = new BookDto();
        bookDto.setRoomId(roomId);
        model.addAttribute("book", bookDto);
        return "book/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        BookDto bookDto = convertToBookDto(bookService.getBook(id).orElseThrow(() -> new BookErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.book.exception.book-not-found"))));
        model.addAttribute("book", bookDto);
        return "book/edit";
    }

    @PostMapping("/new")
    public String saveBook(@ModelAttribute("book") @Valid BookDto bookDto,
                           BindingResult bindingResult) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        bookDto.setClientLogin(login);
        Book book = convertToBook(bookDto);

        if (bindingResult.hasErrors()) {
            throw new BookErrorException(errorHandler.getErrorMessage(bindingResult));
        }
        bookService.save(book);
        return "redirect:/hotel/all";
    }

    @PatchMapping("/{id_book}")
    public String updateBook(@PathVariable("id_book") int id,
                             @ModelAttribute("book") @Valid BookDto bookDto,
                             BindingResult bindingResult) {
        Book updatedBook = convertToBook(bookDto);
        if (bindingResult.hasErrors()) {
            bookDto.setId(id);
            return "book/edit";
        }
        bookService.update(id, updatedBook);
        return "redirect:/hotel/all";
    }

    @DeleteMapping("/{id_book}")
    public String deleteBook(@PathVariable("id_book") int id) {
        bookService.delete(id);
        return "redirect:/hotel/all";
    }

    private BookDto convertToBookDto(Book book) {
        return modelMapper.map(book, BookDto.class);
    }

    private List<BookDto> convertToBookDtoList(List<Book> reviews) {
        Type listType = new TypeToken<List<BookDto>>() {
        }.getType();
        return modelMapper.map(reviews, listType);
    }

    private Book convertToBook(BookDto bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }
}
