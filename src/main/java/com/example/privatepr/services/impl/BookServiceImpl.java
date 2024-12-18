package com.example.privatepr.services.impl;

import lombok.RequiredArgsConstructor;
import com.example.privatepr.models.Book;
import com.example.privatepr.models.Client;
import com.example.privatepr.models.Room;
import com.example.privatepr.repositories.BookRepository;
import com.example.privatepr.services.BookService;
import com.example.privatepr.utils.VerifyingAccess;
import com.example.privatepr.utils.erorsHandler.ErrorHandler;
import com.example.privatepr.utils.exeptions.BookErrorException;
import com.example.privatepr.utils.exeptions.ClientErrorException;
import com.example.privatepr.utils.exeptions.RoomErrorException;
import com.example.privatepr.utils.erorsHandler.validator.BookValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final RoomServiceImpl roomService;
    private final ClientServiceImpl clientService;
    private final BookValidator bookValidator;
    private final VerifyingAccess verifyingAccess;
    private final ErrorHandler errorHandler;

    @Transactional
    public void save(Book book) {
        getValidBook(book);
        verifyingAccess.checkPossibilityAction(book.getClient().getLogin());

        bookRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        Book bookByDeleted = getBook(id).orElseThrow(() -> new BookErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.book.exception.book-not-found")));
        String login = bookByDeleted.getClient().getLogin();
        verifyingAccess.checkPossibilityAction(login);

        bookRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Book bookByUpdate) {
        Book bookInDB = getBook(id).orElseThrow(() -> new BookErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.book.exception.book-not-found")));
        bookByUpdate.setId(id);
        getValidBook(bookByUpdate);
        verifyingAccess.checkPossibilityAction(bookByUpdate.getClient().getLogin(), bookInDB.getClient().getLogin());
        bookRepository.save(bookByUpdate);
    }

    @Transactional
    public List<Book> findAllBookInHotel(int hotelId) {
        return roomService.findAllRoomsByHotelId(hotelId)
                .stream().flatMap(room -> room.getBookList().stream())
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<Book> getBook(int id) {
        return bookRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBook() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Book> findAllByRoomId(long roomId) {
        return bookRepository.findAllByRoomId(roomId);
    }

    @Transactional(readOnly = true)
    public List<Book> findAllByClientId(String login) {
        verifyingAccess.checkPossibilityAction(login);
        return bookRepository.findAllByClientLogin(login);
    }

    private Book getValidBook(Book book) {
        long roomId = book.getRoom().getId();
        Room room = roomService.getRoom(roomId).orElseThrow(() -> new RoomErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.room.exception.rom-not-found-by-id")
                .formatted(roomId)));

        String login = book.getClient().getLogin();
        Client client = clientService.findByLogin(login).orElseThrow(() -> new ClientErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.client.exception.requested-login-not-found")
                .formatted(login)));

        book.setClient(client);
        book.setRoom(room);

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(book, Book.class.getName());
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BookErrorException(errorHandler.getErrorMessage(bindingResult));
        }
        return book;
    }
}
