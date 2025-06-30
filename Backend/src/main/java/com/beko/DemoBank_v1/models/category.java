package com.beko.DemoBank_v1.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., "Food", "Transport"

    private String color; // optional, for frontend use (e.g., pie chart colors)
}
