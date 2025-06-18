package com.example.umm_library_backend.controller;

import com.example.umm_library_backend.dto.borrow_book.BorrowBookRequest;
import com.example.umm_library_backend.dto.borrow_book.BorrowBookResponse;
import com.example.umm_library_backend.model.TransactionEntity;
import com.example.umm_library_backend.service.BorrowServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowController {
    private final BorrowServiceImpl borrowServiceImpl;

    public BorrowController(BorrowServiceImpl borrowServiceImpl) {
        this.borrowServiceImpl = borrowServiceImpl;
    }

    @PostMapping
    public ResponseEntity<BorrowBookResponse> borrowBook(@RequestBody(required = false)BorrowBookRequest borrowBookRequest) {
        TransactionEntity transaction =  borrowServiceImpl.borrowBook(borrowBookRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new BorrowBookResponse<>(200, transaction));
    }
}
