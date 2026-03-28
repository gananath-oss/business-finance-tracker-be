package com.business_finance_tracker.financetracker.controller;

import com.business_finance_tracker.financetracker.dto.request.TransactionRequest;
import com.business_finance_tracker.financetracker.dto.response.ApiResponse;
import com.business_finance_tracker.financetracker.dto.response.TransactionResponse;
import com.business_finance_tracker.financetracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponse>> create(
            @Valid @RequestBody TransactionRequest request
            ) {
        TransactionResponse response = transactionService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Transaction success", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAll(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
            ) {

        List<TransactionResponse> transactions;

        if (startDate != null && endDate != null) {
            transactions = transactionService.getByDateRange(startDate, endDate);
        } else {
            transactions = transactionService.getAllForBusiness();
        }

        return ResponseEntity.ok(
                ApiResponse.success("Transactions retrieved", transactions)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getById(
            @PathVariable Long id) {
        TransactionResponse response = transactionService.getById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Transaction retrieved", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.update(id, request);
        return ResponseEntity.ok(
                ApiResponse.success("Transaction updated", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.success("Transaction deleted", null));
    }
}
