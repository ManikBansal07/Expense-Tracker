package com.beko.DemoBank_v1.controllers;

import com.beko.DemoBank_v1.dto.CreateExpenseRequest;
import com.beko.DemoBank_v1.dto.ExpenseDTO;
import com.beko.DemoBank_v1.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "http://localhost:5173")  // adjust based on your frontend port
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/{userId}")
    public List<ExpenseDTO> getAllExpenses(@PathVariable String userId) {
        return expenseService.getAllExpenses(userId);
    }

    @PostMapping("/{userId}")
    public ExpenseDTO addExpense(@PathVariable String userId, @RequestBody CreateExpenseRequest request) {
        return expenseService.addExpense(userId, request);
    }

    @DeleteMapping("/{expenseId}")
    public void deleteExpense(@PathVariable Long expenseId) {
        expenseService.deleteExpense(expenseId);
    }
}
