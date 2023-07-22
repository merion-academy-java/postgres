package com.example.postgres.user.controller;

import com.example.postgres.user.dto.request.CreateUserRequest;
import com.example.postgres.user.dto.response.UserResponse;
import com.example.postgres.user.entity.UserEntity;
import com.example.postgres.user.exceptions.UserNotFoundException;
import com.example.postgres.user.repository.UserRepository;
import com.example.postgres.user.routes.UserRoutes;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserApiController {

    private final UserRepository userRepository;

    @PostMapping(UserRoutes.CREATE)
    public UserResponse create(@RequestBody CreateUserRequest request) {
        UserEntity user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        user = userRepository.save(user);
        return UserResponse.of(user);
    }

    @GetMapping(UserRoutes.BY_ID)
    public UserResponse byId(@PathVariable Long id) {
        return userRepository
                .findById(id)
                .map(UserResponse::of)
                .orElseThrow(UserNotFoundException::new);
    }

}
