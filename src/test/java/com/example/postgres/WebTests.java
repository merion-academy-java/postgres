package com.example.postgres;

import com.example.postgres.user.dto.request.RegistrationUserRequest;
import com.example.postgres.user.dto.request.EditUserRequest;
import com.example.postgres.user.dto.response.UserResponse;
import com.example.postgres.user.entity.UserEntity;
import com.example.postgres.user.repository.UserRepository;
import com.example.postgres.user.routes.UserRoutes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
@AutoConfigureMockMvc
public class WebTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Value("${init.email}")
    private String initUser;
    @Value("${init.password}")
    private String initPassword;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void config() {
        Optional<UserEntity> check = userRepository.findByEmail(initUser);
        if(check.isPresent()) return;

        UserEntity user = UserEntity.builder()
                .email(initUser)
                .password(passwordEncoder.encode(initPassword))
                .build();
        userRepository.save(user);
    }

    public String authHeader() {
        return "Basic " + Base64.getEncoder().encodeToString((initUser + ":" + initPassword).getBytes());
    }

    @Test
    void contextLoad() throws Exception {
        UserEntity user = UserEntity.builder()
                .firstName("1")
                .lastName("1")
                .build();

        user = userRepository.save(user);

        mockMvc.perform(
                        get(UserRoutes.BY_ID, user.getId().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, authHeader())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void registrationTest() throws Exception {
        RegistrationUserRequest createUserRequest = RegistrationUserRequest.builder()
                .firstName("createTest")
                .lastName("test")
                .email("reg@email.ru")
                .password("1")
                .build();

        mockMvc.perform(
                        post(UserRoutes.REGISTRATION)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createUserRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("createTest")));
    }

    @Test
    void findByIdTest() throws Exception {
        UserEntity user = UserEntity.builder()
                .firstName("findByIdTest")
                .lastName("findByIdTest")
                .build();

        user = userRepository.save(user);

        mockMvc.perform(
                        get(UserRoutes.BY_ID, user.getId().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, authHeader())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("findByIdTest")));
    }

    @Test
    void findByIdTest_notFound() throws Exception {
        mockMvc.perform(
                        get(UserRoutes.BY_ID, "1234")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, authHeader())
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTest() throws Exception {
        UserEntity user = UserEntity.builder()
                .firstName("findByIdTest")
                .lastName("findByIdTest")
                .email("update@email.ru")
                .password(passwordEncoder.encode("1"))
                .build();
        user = userRepository.save(user);

        EditUserRequest request = EditUserRequest.builder()
                .firstName("updateTest")
                .lastName("updateTest")
                .build();

        mockMvc.perform(
                        put(UserRoutes.EDIT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(("update@email.ru" + ":" + "1").getBytes()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("updateTest")));
    }

    @Test
    void deleteTest() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .firstName("findByIdTest")
                .lastName("findByIdTest")
                .build();

        userEntity = userRepository.save(userEntity);

        mockMvc.perform(
                        delete(UserRoutes.BY_ID, userEntity.getId().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, authHeader())
                )

                .andDo(print())
                .andExpect(status().isOk());

        assert userRepository.findById(userEntity.getId()).isEmpty();
    }

    @Test
    void searchTest() throws Exception {

        List<UserResponse> result = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            UserEntity user = UserEntity.builder()
                    .firstName("findByIdTest_" + i)
                    .lastName("findByIdTest_" + i)
                    .build();

            user = userRepository.save(user);
            result.add(UserResponse.of(user));
        }

        // result.remove(0);

        mockMvc.perform(
                        get(UserRoutes.SEARCH)
                                .param("size", "1000")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, authHeader())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)));
    }

}
