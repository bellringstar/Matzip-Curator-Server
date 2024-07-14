package com.matzip.api.domain.user.dto;

import com.matzip.api.domain.user.entity.User;
import com.matzip.api.domain.user.enums.Role;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private String loginId;
    private String email;
    private Set<Role> roles;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;

    public static UserResponseDto toResponse(User user) {
        return new UserResponseDto(
                user.getLoginId(),
                user.getEmail(),
                user.getRoles(),
                user.getCreateDate(),
                user.getModifiedDate()
        );
    }
}
