package com.example.privatepr.repositories;

import com.example.privatepr.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findAllByClientLogin(String login);

    List<Book> findAllByRoomId(long roomId);
}
