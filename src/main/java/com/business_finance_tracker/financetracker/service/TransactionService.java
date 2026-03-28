package com.business_finance_tracker.financetracker.service;

import com.business_finance_tracker.financetracker.dto.request.TransactionRequest;
import com.business_finance_tracker.financetracker.dto.response.TransactionResponse;
import com.business_finance_tracker.financetracker.exception.ResourceNotFoundException;
import com.business_finance_tracker.financetracker.model.Business;
import com.business_finance_tracker.financetracker.model.Category;
import com.business_finance_tracker.financetracker.model.Transaction;
import com.business_finance_tracker.financetracker.model.User;
import com.business_finance_tracker.financetracker.repository.CategoryRepository;
import com.business_finance_tracker.financetracker.repository.TransactionRepository;
import com.business_finance_tracker.financetracker.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final SecurityUtils securityUtils;

    public TransactionService(
            TransactionRepository transactionRepository,
            CategoryRepository categoryRepository,
            SecurityUtils securityUtils
    ) {
        this.transactionRepository  = transactionRepository;
        this.categoryRepository     = categoryRepository;
        this.securityUtils          = securityUtils;
    }

    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        User currentUser    = securityUtils.getCurrentUser();
        Business business   = currentUser.getBusiness();

        // Validate category belongs to tran
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id:: " + request.getCategoryId()
                ));

        if (!category.getBusiness().getId().equals(business.getId())) {
            throw new ResourceNotFoundException(
                    "Category not found with id:: " + request.getCategoryId()
            );
        }

        // Validate category type matches transaction type
        if (!category.getType().equals(request.getType())) {
            throw new IllegalStateException(
                    "Category '" + category.getName() + "' is type "
                            + category.getType() + " but transaction is type "
                            + request.getType()
            );
        }

        // Build entity
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setNote(request.getNote());
        transaction.setCategory(category);
        transaction.setCreatedBy(currentUser);
        transaction.setBusiness(business);

        Transaction saved = transactionRepository.save(transaction);
        return toResponse(saved);
    }

    // Get all
    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllForBusiness() {
        Long businessId = securityUtils.getCurrentBusinessId();
        return transactionRepository.findByBusinessId(businessId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Get by date range
    @Transactional(readOnly = true)
    public List<TransactionResponse> getByDateRange(
            LocalDate startDate, LocalDate endDate) {
        Long businessId = securityUtils.getCurrentBusinessId();
        return transactionRepository
                .findByBusinessIdAndTransactionDateBetween(
                        businessId, startDate, endDate)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Get by id
    @Transactional(readOnly = true)
    public TransactionResponse getById(Long id) {
        Transaction transaction = findTransactionForBusiness(id);
        return toResponse(transaction);
    }

    // Update
    @Transactional
    public TransactionResponse update(Long id, TransactionRequest request) {
        Transaction transaction = findTransactionForBusiness(id);
        Business business       = transaction.getBusiness();

        // Validate category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id:: " + request.getCategoryId()
                ));

        if (!category.getBusiness().getId().equals(business.getId())) {
            throw new ResourceNotFoundException(
                    "Category not found with id:: " + request.getCategoryId()
            );
        }

        if (!category.getType().equals(request.getType())) {
            throw new IllegalStateException(
                    "Category '" + category.getName() + "' is type "
                            + category.getType() + " but transaction is type "
                            + request.getType()
            );
        }

        // Update fields
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setNote(request.getNote());
        transaction.setCategory(category);

        Transaction updated = transactionRepository.save(transaction);
        return toResponse(updated);
    }

    // Delete
    @Transactional
    public void delete(Long id) {
        Transaction transaction = findTransactionForBusiness(id);
        transactionRepository.delete(transaction);
    }

    // ========== Helpers ==========
    private Transaction findTransactionForBusiness(Long id) {
        Long businessId = securityUtils.getCurrentBusinessId();

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction not found with id:: " + id
                ));

        if (!transaction.getBusiness().getId().equals(businessId)) {
            throw new ResourceNotFoundException(
                    "Transaction not found with id:: " + id
            );
        }

        return transaction;
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCategory().getName(),
                transaction.getNote(),
                transaction.getCreatedBy().getName(),
                transaction.getCreatedAt()
        );
    }
}
