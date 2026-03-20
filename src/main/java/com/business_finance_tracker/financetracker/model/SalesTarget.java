package com.business_finance_tracker.financetracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales_targets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal targetAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetPeriod period;

    @Column(nullable = false)
    private Integer targetMonth;   // 1-12 (null if DAILY)

    @Column(nullable = false)
    private Integer targetYear;    // e.g. 2025

    @Column(nullable = false)
    private Integer targetDay;     // 1-31 (null if MONTHLY)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;     // null = target for ALL categories

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
