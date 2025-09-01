package com.api.shosetsuya.models.entities;

import com.api.shosetsuya.models.enums.NovelStatus;
import com.api.shosetsuya.models.enums.NovelType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "novel_translations")
public class NovelTranslation {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false)
    private UUID novelId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NovelStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NovelType type;

    @Column(nullable = false)
    private String authorName;

    private String coverImageUrl;
    private String description;
    private String artistName;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<NovelTitle> titles = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "novel_categories",
        joinColumns = @JoinColumn(name = "novel_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>(); // bi-directional

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<NovelRating> ratings = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "translation_group_id", nullable = false)
    private TranslationGroup translationGroup;
}
