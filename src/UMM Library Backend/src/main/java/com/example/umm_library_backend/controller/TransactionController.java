package com.example.umm_library_backend.controller;

import com.example.umm_library_backend.dto.transaction.DeleteTransactionResponse;
import com.example.umm_library_backend.dto.transaction.RetrieveTransactionResponse;
import com.example.umm_library_backend.dto.transaction.UpdateTransactionRequest;
import com.example.umm_library_backend.dto.transaction.UpdateTransactionResponse;
import com.example.umm_library_backend.model.TransactionEntity;
import com.example.umm_library_backend.service.TransactionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionServiceImpl transactionServiceImpl;

    public TransactionController(TransactionServiceImpl transactionServiceImpl) {
        this.transactionServiceImpl = transactionServiceImpl;
    }

    @GetMapping
    public ResponseEntity<RetrieveTransactionResponse> getAll() {
        return ResponseEntity.ok().body(new RetrieveTransactionResponse<>(200, transactionServiceImpl.getAllTransactions()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateTransactionResponse> updateTransaction(@PathVariable long id, @RequestBody(required = false) UpdateTransactionRequest updateTransactionRequest) {
        TransactionEntity trx = transactionServiceImpl.partialUpdateTx(id, updateTransactionRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateTransactionResponse<>(200, trx));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteTransactionResponse> deleteTransaction(@PathVariable long id) {
        transactionServiceImpl.deleteTransaction(id);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteTransactionResponse<>(200, id));
    }
}
