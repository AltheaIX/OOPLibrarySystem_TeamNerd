package com.example.umm_library_backend.controller;

import com.example.umm_library_backend.dto.auth.LoginResponse;
import com.example.umm_library_backend.dto.book.*;
import com.example.umm_library_backend.model.BooksEntity;
import com.example.umm_library_backend.service.BookServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {
    private final BookServiceImpl bookService;

    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<CreateBookResponse> addBook(@RequestBody(required = false) CreateBookRequest book) {
        BooksEntity createdBook = bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.OK).body(new CreateBookResponse<>(200, createdBook));
    }

    @GetMapping
    public ResponseEntity<RetrieveBookResponse> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(new RetrieveBookResponse<>(200, bookService.getAllBooks()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RetrieveBookResponse> getBookById(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(new RetrieveBookResponse<>(200, bookService.getBookById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateBookResponse> updateBook(@PathVariable Long id, @RequestBody BooksEntity book) {
        BooksEntity updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateBookResponse<>(200, updatedBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteBookResponse> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteBookResponse<>(200, true));
    }
}
