package com.atbm.projecttlkrbe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "admin_stroke_option")
@Data
public class AdminStrokeOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_stroke_data_id")
    private AdminStrokeData adminStrokeData;

    private String content;
    private boolean accepted;
}
