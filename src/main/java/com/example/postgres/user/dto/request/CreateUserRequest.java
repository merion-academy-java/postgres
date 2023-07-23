package com.example.postgres.user.dto.request;

import com.example.postgres.user.exceptions.BadRequestException;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserRequest {
    private String lastName;
    private String firstName;
    private String email;
    private String password;

    public void validate() throws BadRequestException {
        if(email == null || email.isBlank()) throw new BadRequestException();
        if(password == null || password.isBlank()) throw new BadRequestException();
    }
}
