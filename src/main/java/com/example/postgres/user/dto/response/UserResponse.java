package com.example.postgres.user.dto.response;

import com.example.postgres.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserResponse extends UserEntity {

    public static UserResponse of(UserEntity entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .build();
    }

}
