package com.business_finance_tracker.financetracker.service;

import com.business_finance_tracker.financetracker.dto.request.CategoryRequest;
import com.business_finance_tracker.financetracker.dto.response.CategoryResponse;
import com.business_finance_tracker.financetracker.exception.ResourceNotFoundException;
import com.business_finance_tracker.financetracker.model.Business;
import com.business_finance_tracker.financetracker.model.Category;
import com.business_finance_tracker.financetracker.model.User;
import com.business_finance_tracker.financetracker.repository.CategoryRepository;
import com.business_finance_tracker.financetracker.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final SecurityUtils securityUtils;

    public CategoryService(CategoryRepository categoryRepository, SecurityUtils securityUtils) {
        this.categoryRepository = categoryRepository;
        this.securityUtils = securityUtils;
    }

    // Create
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        User currentUser    = securityUtils.getCurrentUser();
        Business business   = currentUser.getBusiness();

        // Check duplications
        if (categoryRepository.existsByNameAndBusinessId(
                request.getName(), business.getId()
        )) {
            throw new IllegalStateException(
                    "Category " + request.getName()
                    + " already exist in your business"
            );
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setType(request.getType());
        category.setBusiness(business);

        Category saved = categoryRepository.save(category);
        return toResponse(saved);
    }

    // Get all
    public List<CategoryResponse> getAllForBusiness() {
        Long businessId = securityUtils.getCurrentBusinessId();
        return categoryRepository.findByBusinessId(businessId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Get one
    public CategoryResponse getById(Long id) {
        Category category = findCategoryForBusiness(id);
        return toResponse(category);
    }

    // Update
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = findCategoryForBusiness(id);

        // Check duplications
        if (!category.getName().equals(request.getName())
            && categoryRepository.existsByNameAndBusinessId(
                    request.getName(), category.getBusiness().getId()
        )) {
            throw new IllegalStateException(
                    "Category " + request.getName()
                    + " already exist in your business"
            );
        }

        category.setName(request.getName());
        category.setType(request.getType());

        Category updated = categoryRepository.save(category);
        return toResponse(updated);
    }

    // Delete
    @Transactional
    public void delete(Long id) {
        Category category = findCategoryForBusiness(id);
        categoryRepository.delete(category);
    }

    // ========== Helpers ==========
    // Find category and check it belongs to the current business
    public Category findCategoryForBusiness(Long id) {
        Long businessId = securityUtils.getCurrentBusinessId();

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id:: " + id
                ));

        if (!category.getBusiness().getId().equals(businessId)) {
            throw new IllegalStateException(
                    "Category not found with id:: " + id
            );
        }

        return category;
    }

    // Convert to response DTO
    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getType()
        );
    }
}
