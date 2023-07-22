package com.example.postgres.user.dto.request;

import lombok.Getter;

@Getter
public class EditUserRequest {
    private Long id;
    private String lastName;
    private String firstName;
}
