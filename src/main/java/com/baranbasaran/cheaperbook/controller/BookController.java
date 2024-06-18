package com.baranbasaran.cheaperbook.controller;

import com.baranbasaran.cheaperbook.common.dto.Response;
import com.baranbasaran.cheaperbook.controller.request.Book.CreateBookRequest;
import com.baranbasaran.cheaperbook.controller.request.Book.UpdateBookRequest;
import com.baranbasaran.cheaperbook.dto.BookDto;
import com.baranbasaran.cheaperbook.service.AuthenticationService;
import com.baranbasaran.cheaperbook.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthenticationService authenticationService;

    @GetMapping("/api/search")
    public Response<BookDto> getBookByIsbn(@RequestParam String isbn) {
        return Response.success(bookService.getBookByIsbn(isbn));
    }
    @GetMapping
    public Response<List<BookDto>> getBooksByUserId() {
        return Response.success(bookService.findAllBooksByUserId(authenticationService.getAuthenticatedUser().getId()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response<BookDto> createBookForUser(@RequestBody @Valid CreateBookRequest bookRequest) {
        return Response.success(bookService.addBookUser(
            authenticationService.getAuthenticatedUser().getId(),
            bookRequest));
    }

    @GetMapping("{bookId}")
    public Response<BookDto> getBookByUserIdAndBookId(@PathVariable Long bookId) {
        return Response.success(bookService.findBookByIdAndUserId(
            bookId,
            authenticationService.getAuthenticatedUser().getId()
        ));
    }


    @PutMapping("{bookId}")
    public Response<BookDto> updateBookForUser(@PathVariable Long bookId, @RequestBody @Valid UpdateBookRequest bookRequest) {
        return Response.success(bookService.updateBookForUser(bookId, bookRequest));
    }

    @DeleteMapping("{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long bookId) {
        bookService.deleteBookForUser(authenticationService.getAuthenticatedUser().getId(), bookId);
    }

}