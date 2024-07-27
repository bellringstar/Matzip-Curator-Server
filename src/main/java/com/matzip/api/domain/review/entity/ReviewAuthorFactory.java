package com.matzip.api.domain.review.entity;

import com.matzip.api.domain.review.entity.vo.ReviewAuthor;
import com.matzip.api.domain.user.entity.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ReviewAuthorFactory {

    public ReviewAuthor createFromSecurityContext(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new IllegalArgumentException("Unexpected principal type");
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return new ReviewAuthor(userPrincipal.getUser().getId(), userPrincipal.getUser().getLoginId(),
                userPrincipal.getUser().getName());
    }
}
