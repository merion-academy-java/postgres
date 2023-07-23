package com.example.postgres.user.dto.response;

import com.example.postgres.user.entity.UserEntity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserResponse {
    protected Long id;
    protected String firstName;
    protected String lastName;
    protected String email;

    public static UserResponse of(UserEntity entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .build();
    }

}
