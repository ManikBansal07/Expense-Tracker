package com.beko.DemoBank_v1.services;

import com.beko.DemoBank_v1.dto.CreateExpenseRequest;
import com.beko.DemoBank_v1.dto.ExpenseDTO;
import com.beko.DemoBank_v1.exception.ResourceNotFoundException;
import com.beko.DemoBank_v1.models.Category;
import com.beko.DemoBank_v1.models.Expense;
import com.beko.DemoBank_v1.models.User;
import com.beko.DemoBank_v1.repository.CategoryRepository;
import com.beko.DemoBank_v1.repository.ExpenseRepository;
import com.beko.DemoBank_v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ExpenseDTO> getAllExpenses(String userId) {
        List<Expense> expenses = expenseRepository.findByUser_User_id(userId);
        return expenses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ExpenseDTO addExpense(String userId, CreateExpenseRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.getCategoryId()));

        Expense expense = new Expense();
        expense.setUser(user);
        expense.setCategory(category);
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setDescription(request.getDescription());

        Expense saved = expenseRepository.save(expense);
        return convertToDTO(saved);
    }

    public void deleteExpense(Long expenseId) {
        expenseRepository.deleteById(expenseId);
    }

    public List<ExpenseDTO> getExpensesByCategory(String userId, String categoryName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        List<Expense> expenses = expenseRepository.findByUserAndCategory_Name(user, categoryName);
        return expenses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ExpenseDTO> getExpensesByDateRange(String userId, String start, String end) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        List<Expense> expenses = expenseRepository.findByUserAndDateBetween(user, startDate, endDate);
        return expenses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ExpenseDTO> getExpensesByDateAndCategory(String userId, String start, String end, String categoryName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        List<Expense> expenses = expenseRepository.findByUserAndDateBetween(user, startDate, endDate)
                .stream()
                .filter(e -> e.getCategory().getName().equalsIgnoreCase(categoryName))
                .collect(Collectors.toList());
        return expenses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Map<String, Object> getMonthlySummary(String userId, String monthStr) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        YearMonth ym = YearMonth.parse(monthStr);  // "2025-06"
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<ExpenseDTO> expenses = getExpensesByDateRange(userId, start.toString(), end.toString());

        double total = expenses.stream().mapToDouble(ExpenseDTO::getAmount).sum();
        Map<String, Double> byCategory = expenses.stream()
                .collect(Collectors.groupingBy(
                        ExpenseDTO::getCategoryName,
                        Collectors.summingDouble(ExpenseDTO::getAmount)
                ));

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("byCategory", byCategory);
        return result;
    }

    public ExpenseDTO convertToDTO(Expense expense) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setAmount(expense.getAmount());
        dto.setDescription(expense.getDescription());
        dto.setDate(expense.getDate());
        dto.setCategoryId(expense.getCategory().getId());
        dto.setCategoryName(expense.getCategory().getName());
        return dto;
    }
}
