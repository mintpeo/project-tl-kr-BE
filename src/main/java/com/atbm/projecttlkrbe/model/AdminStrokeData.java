package com.atbm.projecttlkrbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "admin_stroke_data")
@Data
public class AdminStrokeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "char_id")
    @JsonIgnore
    private CharacterEntity character;

    @Enumerated(EnumType.STRING)
    private AdminStrokeStatus status;

    private LocalDate updatedAt;
    private String note;
    private String activePublicId;
    private String activeUrl;
    private String pendingPublicId;
    private String pendingUrl;
}