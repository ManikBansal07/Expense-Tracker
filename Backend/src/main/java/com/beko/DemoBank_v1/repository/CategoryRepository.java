package com.beko.DemoBank_v1.repository;

import com.beko.DemoBank_v1.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
