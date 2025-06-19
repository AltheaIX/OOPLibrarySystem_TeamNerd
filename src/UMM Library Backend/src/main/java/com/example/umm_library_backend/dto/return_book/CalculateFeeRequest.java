package com.example.umm_library_backend.dto.return_book;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CalculateFeeRequest {
    private LocalDate returnDate;

    public CalculateFeeRequest(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
