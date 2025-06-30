package com.beko.DemoBank_v1.controllers;

import com.beko.DemoBank_v1.dto.CreateExpenseRequest;
import com.beko.DemoBank_v1.dto.ExpenseDTO;
import com.beko.DemoBank_v1.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "http://localhost:5173")  // adjust based on your frontend port
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // 🔹 Fetch all expenses for a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses(@PathVariable String userId) {
        List<ExpenseDTO> expenses = expenseService.getAllExpenses(userId);
        return ResponseEntity.ok(expenses);
    }

    // 🔹 Add a new expense for a user (with validation)
    @PostMapping("/{userId}")
    public ResponseEntity<ExpenseDTO> addExpense(
            @PathVariable String userId,
            @RequestBody @Valid CreateExpenseRequest request) {
        ExpenseDTO savedExpense = expenseService.addExpense(userId, request);
        return ResponseEntity.status(201).body(savedExpense);
    }

    // 🔹 Delete an expense by ID
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long expenseId) {
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.noContent().build();
    }

        // 🔍 Filter by Category
    @GetMapping("/{userId}/category/{categoryName}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByCategory(
            @PathVariable String userId,
            @PathVariable String categoryName) {
        List<ExpenseDTO> expenses = expenseService.getExpensesByCategory(userId, categoryName);
        return ResponseEntity.ok(expenses);
    }

    // 📅 Filter by Date Range
    @GetMapping("/{userId}/filter/date")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByDateRange(
            @PathVariable String userId,
            @RequestParam String start,
            @RequestParam String end) {
        List<ExpenseDTO> expenses = expenseService.getExpensesByDateRange(userId, start, end);
        return ResponseEntity.ok(expenses);
    }

    // 🔁 Filter by Date Range + Category
    @GetMapping("/{userId}/filter/date-category")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByDateAndCategory(
            @PathVariable String userId,
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam String category) {
        List<ExpenseDTO> expenses = expenseService.getExpensesByDateAndCategory(userId, start, end, category);
        return ResponseEntity.ok(expenses);
    }

}
