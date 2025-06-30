package com.beko.DemoBank_v1.services;

import com.beko.DemoBank_v1.dto.CreateExpenseRequest;
import com.beko.DemoBank_v1.dto.ExpenseDTO;
import com.beko.DemoBank_v1.models.Category;
import com.beko.DemoBank_v1.models.Expense;
import com.beko.DemoBank_v1.models.User;
import com.beko.DemoBank_v1.repository.CategoryRepository;
import com.beko.DemoBank_v1.repository.ExpenseRepository;
import com.beko.DemoBank_v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Category> categoryOptional = categoryRepository.findById(request.getCategoryId());

        if (!userOptional.isPresent() || !categoryOptional.isPresent()) {
            throw new RuntimeException("User or Category not found");
        }

        Expense expense = new Expense();
        expense.setUser(userOptional.get());
        expense.setCategory(categoryOptional.get());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setDescription(request.getDescription());

        Expense saved = expenseRepository.save(expense);
        return convertToDTO(saved);
    }

    public void deleteExpense(Long expenseId) {
        expenseRepository.deleteById(expenseId);
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
