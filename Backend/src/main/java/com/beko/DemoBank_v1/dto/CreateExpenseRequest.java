package com.beko.DemoBank_v1.dto;

import java.time.LocalDate;

public class CreateExpenseRequest {
    private String description;
    private Double amount;
    private LocalDate date;
    private Long categoryId;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
