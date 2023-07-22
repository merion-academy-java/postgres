package com.example.postgres.user.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class CreateUserRequest {
    private String lastName;
    private String firstName;
}
