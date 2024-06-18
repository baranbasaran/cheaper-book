package com.baranbasaran.cheaperbook.service;
import com.baranbasaran.cheaperbook.client.GoogleBookApiClient;
import com.baranbasaran.cheaperbook.controller.request.Book.BookRequest;
import com.baranbasaran.cheaperbook.controller.request.Book.CreateBookRequest;
import com.baranbasaran.cheaperbook.controller.request.Book.UpdateBookRequest;
import com.baranbasaran.cheaperbook.dto.BookDto;
import com.baranbasaran.cheaperbook.enums.Status;
import com.baranbasaran.cheaperbook.exception.BookNotFoundException;
import com.baranbasaran.cheaperbook.model.Book;
import com.baranbasaran.cheaperbook.model.User;
import com.baranbasaran.cheaperbook.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private final GoogleBookApiClient bookApiClient;
    private final BookRepository bookRepository;
    private final UserService userService;

    public BookDto getBookByIsbn(String isbn) {
        return BookDto.from(getBookByIsbnFromApi(isbn));
    }
    @Transactional
    private Book mergeBookRequestWithExistingBook(Long userId ,BookRequest request) {
        Book book = null;
        if (request.getId() != null) {
            book = bookRepository.findById(request.getId())
                    .orElseThrow(() -> new BookNotFoundException(request.getId()));
        }
        if (book == null) {
            book = getBookByIsbnFromApi(request.getIsbn());
        }
        User user = userService.getUserById(userId);
        book.setOwner(user);
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setStatus(Status.AVAILABLE);
        return bookRepository.save(book);
    }

    private Book getBookByIsbnFromApi(String isbn) {
        Optional<Book> book = bookApiClient.getBookDataByIsbn(isbn).getBook();
        return book.orElse(new Book());
    }

    public List<BookDto> findAllBooksByUserId(Long userId) {
        return bookRepository.findAllByOwnerId(userId).stream().map(BookDto::from).toList();
    }

    public BookDto findBookByIdAndUserId(Long bookId, Long userId) {
        Book book = bookRepository.findByIdAndOwnerId(bookId, userId).orElseThrow(() -> new BookNotFoundException(bookId));
        return BookDto.from(book);
    }

    public BookDto addBookUser(Long userId, CreateBookRequest bookRequest) {
        return BookDto.from(mergeBookRequestWithExistingBook(userId, bookRequest));
    }

    public BookDto updateBookForUser(Long bookId, UpdateBookRequest bookRequest) {
        bookRequest.setId(bookId);
        return BookDto.from(updateBook(bookRequest));
    }

    @Transactional
    public Book updateBook(UpdateBookRequest request) {
        Book book = bookRepository.findById(request.getId())
                .orElseThrow(() -> new BookNotFoundException(request.getId()));

        if (request.getIsbn() != null) {
            book = getBookByIsbnFromApi(request.getIsbn());
        }
        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            book.setAuthor(request.getAuthor());
        }
        if (request.getDescription() != null) {
            book.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            book.setPrice(request.getPrice());
        }
        return bookRepository.save(book);
    }

    public void deleteBookForUser(Long userId, Long bookId) {
        Book book = bookRepository.findByIdAndOwnerId(bookId, userId).orElseThrow(() -> new BookNotFoundException(bookId));
        book.setDeleted(true);
        bookRepository.save(book);
    }
}
