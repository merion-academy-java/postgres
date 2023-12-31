package com.example.postgres.user.controller;

import com.example.postgres.user.dto.request.RegistrationUserRequest;
import com.example.postgres.user.dto.request.EditUserRequest;
import com.example.postgres.user.dto.response.UserResponse;
import com.example.postgres.user.entity.UserEntity;
import com.example.postgres.user.exceptions.BadRequestException;
import com.example.postgres.user.exceptions.UserAlreadyExistException;
import com.example.postgres.user.exceptions.UserNotFoundException;
import com.example.postgres.user.repository.UserRepository;
import com.example.postgres.user.routes.UserRoutes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${init.email}")
    private String initUser;
    @Value("${init.password}")
    private String initPassword;

    @GetMapping(UserRoutes.INIT)
    public UserResponse init() {
        Optional<UserEntity> checkUser = userRepository.findByEmail(initUser);
        UserEntity user;
        if (checkUser.isEmpty()) {
            user = UserEntity.builder()
                    .firstName("Default user")
                    .lastName("Default user")
                    .email(initUser)
                    .password(passwordEncoder.encode(initPassword))
                    .build();
            user = userRepository.save(user);
        } else {
            user = checkUser.get();
        }

        return UserResponse.of(user);
    }

    @Operation(summary = "Регистрация пользователя", description = "Создаем пользователя по имени и фамилии")
    @PostMapping(UserRoutes.REGISTRATION)
    public UserResponse registration(@RequestBody RegistrationUserRequest request) throws BadRequestException, UserAlreadyExistException {
        request.validate();

        Optional<UserEntity> check = userRepository.findByEmail(request.getEmail());
        if(check.isPresent()) throw new UserAlreadyExistException();

        UserEntity user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        user = userRepository.save(user);
        return UserResponse.of(user);
    }

    @Operation(summary = "Редактирование пользоваеля пользователя", description = "Создаем пользователя по имени и фамилии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно отредактированный пользователь",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Не корректный запрос",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким id не найден",
                    content = @Content)})
    @PutMapping(UserRoutes.EDIT)
    public UserResponse edit(Principal principal, @RequestBody EditUserRequest request) {
        UserEntity user = userRepository
                .findByEmail(principal.getName())
                .orElseThrow(UserNotFoundException::new);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

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

    @DeleteMapping(UserRoutes.BY_ID)
    public String delete(@PathVariable Long id) {
        userRepository.deleteById(id);
        return HttpStatus.OK.name();
    }

    @GetMapping(UserRoutes.SEARCH)
    public List<UserResponse> search(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        ExampleMatcher ignoringExampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<UserEntity> example = Example.of(
                UserEntity.builder().lastName(query).firstName(query).build(),
                ignoringExampleMatcher);

        return userRepository
                .findAll(example, pageable)
                .stream()
                .map(UserResponse::of)
                .collect(Collectors.toList());
    }

}
