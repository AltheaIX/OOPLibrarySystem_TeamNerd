package com.example.umm_library_backend.controller;

import com.example.umm_library_backend.dto.return_book.CalculateFeeRequest;
import com.example.umm_library_backend.dto.return_book.CalculateFeeResponse;
import com.example.umm_library_backend.dto.return_book.ReturnBookRequest;
import com.example.umm_library_backend.dto.return_book.ReturnBookResponse;
import com.example.umm_library_backend.service.ReturnService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/return")
public class ReturnController {
    private final ReturnService returnService;

    public ReturnController(ReturnService returnService) {
        this.returnService = returnService;
    }

    @PostMapping("")
    public ResponseEntity<ReturnBookResponse> returnBook(@RequestBody(required = true) ReturnBookRequest returnBookRequest) {
        returnService.returnBook(returnBookRequest.getTransactionId());
        return ResponseEntity.status(HttpStatus.OK).body(new ReturnBookResponse<>(200, "OK"));
    }

    @PostMapping("/calculate-fee")
    public ResponseEntity<CalculateFeeResponse> calculateFee(@RequestBody(required=false) CalculateFeeRequest request) {
        long dayPassed = returnService.dayPassed(request.getReturnDate());
        return ResponseEntity.status(HttpStatus.OK).body(new CalculateFeeResponse<>(200, returnService.calculateFee(dayPassed)));
    }
}
