package com.example.postgres.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditUserRequest {
    private String lastName;
    private String firstName;
}
