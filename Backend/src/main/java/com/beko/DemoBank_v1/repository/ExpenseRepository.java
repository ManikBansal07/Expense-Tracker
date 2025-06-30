package com.beko.DemoBank_v1.repository;

import com.beko.DemoBank_v1.models.Expense;
import com.beko.DemoBank_v1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);
    List<Expense> findByUser_User_id(String userId);
    List<Expense> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);
    List<Expense> findByUserAndCategory_Name(User user, String categoryName);
}
