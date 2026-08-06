package com.atbm.projecttlkrbe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "auth_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Auth auth;

    private String fullName;
    private String numberPhone;
}
