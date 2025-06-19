package com.example.umm_library_backend.dto.transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTransactionRequest {
    private long userId;
    private long bookId;
    private String returnDate;
    private String status;
}
