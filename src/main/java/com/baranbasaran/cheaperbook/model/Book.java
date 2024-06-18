package com.baranbasaran.cheaperbook.model;

import com.baranbasaran.cheaperbook.common.model.BaseEntity;
import com.baranbasaran.cheaperbook.dto.BookDto;
import com.baranbasaran.cheaperbook.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity(name = "books")
@Table(name = "books")
@Where(clause = "deleted = false")
public class Book extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @ElementCollection
    @Column(nullable = false)
    private List<String> genre;

    @Column(nullable = false, length = 1000, columnDefinition = "TEXT")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private String isbn;

    @Column
    private String coverImage;

    @Column
    private String publisher;

    @Column
    private Integer publicationYear;

    public void transferOwnership(User newOwner) {
        this.owner = newOwner;
    }

    public Book mergeFromDto(BookDto book) {
        if (book.getTitle() != null) {
            this.setTitle(book.getTitle());
        }
        if (book.getAuthor() != null) {
            this.setAuthor(book.getAuthor());
        }
        if (book.getGenre() != null) {
            this.setGenre(book.getGenre());
        }
        if (book.getDescription() != null) {
            this.setDescription(book.getDescription());
        }
        if (book.getPrice() != null) {
            this.setPrice(book.getPrice());
        }
        if (book.getStatus() != null) {
            this.setStatus(book.getStatus());
        }
        if (book.getIsbn() != null) {
            this.setIsbn(book.getIsbn());
        }
        if (book.getCoverImage() != null) {
            this.setCoverImage(book.getCoverImage());
        }
        if (book.getPublisher() != null) {
            this.setPublisher(book.getPublisher());
        }
        if (book.getPublicationYear() != null) {
            this.setPublicationYear(book.getPublicationYear());
        }
        return this;
    }

    public boolean isValid() {
        return this.getTitle() != null;
    }
}
