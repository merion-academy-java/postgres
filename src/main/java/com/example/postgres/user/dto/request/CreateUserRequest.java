package com.example.postgres.user.dto.request;

import lombok.Data;
import lombok.Getter;

@Getter
public class CreateUserRequest {
    private String lastName;
    private String firstName;
}
