package com.example.postgres.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditUserRequest {
    private Long id;
    private String lastName;
    private String firstName;
}
