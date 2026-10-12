package com.atbm.projecttlkrbe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "achievement")
@Data
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
}