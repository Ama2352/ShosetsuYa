package com.api.shosetsuya.models.entities;

import com.api.shosetsuya.models.enums.TranslationGroupRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "translation_group_members")
public class TranslationGroupMember {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false)
    private UUID memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private TranslationGroup translationGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "translation_group_member_roles", joinColumns = @JoinColumn(name = "member_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Set<TranslationGroupRole> roles;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime joinedAt;
}
