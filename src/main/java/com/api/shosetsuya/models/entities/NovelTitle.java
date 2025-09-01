package com.api.shosetsuya.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "novel_titles")
public class NovelTitle {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false)
    private UUID titleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", nullable = false)
    private NovelTranslation novel;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private boolean isMainTitle;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public NovelTitle(String title, String language, boolean isMainTitle) {
        this.title = title;
        this.language = language;
        this.isMainTitle = isMainTitle;
    }
}
