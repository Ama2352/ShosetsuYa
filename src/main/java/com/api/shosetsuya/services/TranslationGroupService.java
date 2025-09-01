package com.api.shosetsuya.services;

import com.api.shosetsuya.helpers.exceptions.ResourceNotFoundException;
import com.api.shosetsuya.models.dtos.translation_group.CreateTranslationGroupRequest;
import com.api.shosetsuya.models.entities.TranslationGroup;
import com.api.shosetsuya.models.entities.TranslationGroupMember;
import com.api.shosetsuya.models.entities.User;
import com.api.shosetsuya.models.enums.TranslationGroupRole;
import com.api.shosetsuya.repositories.TranslationGroupMemberRepo;
import com.api.shosetsuya.repositories.TranslationGroupRepo;
import com.api.shosetsuya.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TranslationGroupService {

    private final TranslationGroupRepo translationGroupRepo;
    private final UserService userService;
    private final TranslationGroupMemberRepo translationGroupMemberRepo;
    private final MessageSource messageSource;
    private final UserRepo userRepo;

    public TranslationGroup getTranslationGroupById(UUID groupId) {
        return translationGroupRepo.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException(
                            messageSource.getMessage("error.translation.group.not-found",
                                    null,
                                    LocaleContextHolder.getLocale()
                            )
                        )
                );
    }

    public List<TranslationGroup> getAllTranslationGroups() {
        return translationGroupRepo.findAll();
    }

    @Transactional
    public void createTranslationGroup(CreateTranslationGroupRequest request) {
        User currentUser = userService.getCurrentUser();
        if(translationGroupRepo.existsByName(request.getName())) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("error.translation.group.name-exists",
                            null,
                            LocaleContextHolder.getLocale()
                    )
            );
        }

        TranslationGroup group = translationGroupRepo.save(new TranslationGroup(
                request.getName(),
                request.getAvatarUrl(),
                request.getBio()
            )
        );

        TranslationGroupMember member = new TranslationGroupMember();
        member.setRoles(Set.of(TranslationGroupRole.ADMIN, TranslationGroupRole.TRANSLATOR));

        // Keep both sides of the relationship in sync
        group.addMember(member);
        currentUser.addGroupMember(member);

        translationGroupMemberRepo.save(member);
    }

    @Transactional
    public void addTranslationGroupMember(UUID groupId, UUID userId) {
        User user = userService.findById(userId);
        TranslationGroup group = getTranslationGroupById(groupId);

        TranslationGroupMember newMember = new TranslationGroupMember();
        newMember.setRoles(Set.of(TranslationGroupRole.TRANSLATOR));

        user.addGroupMember(newMember);
        group.addMember(newMember);

        translationGroupMemberRepo.save(newMember);
    }
}
