package com.example.umm_library_backend.dto.return_book;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnBookRequest {
    private long transactionId;

    public ReturnBookRequest(long transactionId) {
        this.transactionId = transactionId;
    }
}
