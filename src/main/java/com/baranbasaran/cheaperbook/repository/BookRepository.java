package com.baranbasaran.cheaperbook.repository;

import com.baranbasaran.cheaperbook.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByOwnerId(Long ownerId);

    Optional<Book> findByIdAndOwnerId(Long id, Long ownerId);
}