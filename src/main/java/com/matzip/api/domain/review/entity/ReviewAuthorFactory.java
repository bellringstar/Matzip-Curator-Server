package com.matzip.api.domain.review.entity;

import com.matzip.api.domain.review.entity.vo.ReviewAuthor;
import com.matzip.api.domain.user.entity.UserPrincipal;
import java.util.Random;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ReviewAuthorFactory {

    private static final String[] ADJECTIVES = {"Happy", "Clever", "Brave", "Gentle", "Wise"};
    private static final String[] NOUNS = {"Panda", "Tiger", "Eagle", "Dolphin", "Fox"};
    private static final Random RANDOM = new Random();

    public ReviewAuthor createFromSecurityContext(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new IllegalArgumentException("Unexpected principal type");
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return new ReviewAuthor(userPrincipal.getUser().getId(), generateRandomName());
    }

    private String generateRandomName() {
        String adjective = ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[RANDOM.nextInt(NOUNS.length)];
        return adjective + noun + RANDOM.nextInt(100);
    }
}
