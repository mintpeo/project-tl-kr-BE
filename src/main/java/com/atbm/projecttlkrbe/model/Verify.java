package com.atbm.projecttlkrbe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "verify")
@Data
public class Verify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "auth_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Auth auth;

    private LocalDateTime expiredAt;
    private String code;
    private boolean status;
}
