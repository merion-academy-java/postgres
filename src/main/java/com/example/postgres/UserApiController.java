package com.example.postgres;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserApiController {

    private final UserRepository userRepository;

    @GetMapping("/")
    public UserEntity test(){
        UserEntity user = UserEntity.builder()
                .firstName("Test")
                .lastName("Test")
                .build();

        user = userRepository.save(user);
        return user;
    }

}
