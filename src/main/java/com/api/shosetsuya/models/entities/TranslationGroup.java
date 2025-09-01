package com.api.shosetsuya.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "translation_groups")
public class TranslationGroup {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false)
    private UUID groupId;

    @OneToMany(mappedBy = "translationGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<NovelTranslation> novels = new HashSet<>();

    @Column(nullable = false, unique = true)
    private String name;

    private String avatarUrl;

    private String bio;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "translationGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TranslationGroupMember> members = new HashSet<>();

    public TranslationGroup(String name, String avatarUrl, String bio) {
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
    }

    public void addMember(TranslationGroupMember member) {
        members.add(member);
        member.setTranslationGroup(this);
    }
}
