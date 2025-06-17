package com.example.umm_library_backend.service;

import java.time.LocalDate;

public interface ReturnService {
    public long dayPassed(LocalDate returnDate);
    public long calculateFee(long dayPassed);
    public void returnBook(Long transactionId);
}
