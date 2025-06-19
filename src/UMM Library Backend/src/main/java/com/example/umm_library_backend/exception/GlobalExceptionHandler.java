package com.example.umm_library_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailExists(EmailAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(
                Map.of("status", 400, "message", e.getMessage())
        );
    }

    @ExceptionHandler(DataNotExistsException.class)
    public ResponseEntity<?> handleDataNotExists(DataNotExistsException e) {
        return ResponseEntity.badRequest().body(
                Map.of("status", 400, "message", e.getMessage())
        );
    }

    @ExceptionHandler(NotEnoughQuantityException.class)
    public ResponseEntity<?> handleNotEnoughQuantity(NotEnoughQuantityException e) {
        return ResponseEntity.badRequest().body(
                Map.of("status", 400, "message", e.getMessage())
        );
    }

    @ExceptionHandler(BookAlreadyBorrowedException.class)
    public ResponseEntity<?> handleBookAlreadyBorrowed(BookAlreadyBorrowedException e) {
        return ResponseEntity.badRequest().body(
                Map.of("status", 400, "message", e.getMessage())
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of("status", 401, "message", e.getMessage())
        );
    }
}
