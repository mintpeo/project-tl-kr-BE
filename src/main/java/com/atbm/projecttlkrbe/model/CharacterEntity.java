package com.atbm.projecttlkrbe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "characters")
@Data
public class CharacterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String transcription;
    private Integer strokeCount;
    private String strokeSvgUrl;
    private String strokeNotArrowSvgUrl;
    private boolean isDouble;
    private String imageVersion;

    @Enumerated(EnumType.STRING)
    private CharacterType type;
}
