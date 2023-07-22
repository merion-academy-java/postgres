package com.example.postgres;

import com.example.postgres.user.dto.response.UserResponse;
import com.example.postgres.user.entity.UserEntity;
import com.example.postgres.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostgresApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void repositoryTest() {
		UserEntity user = UserEntity.builder()
				.lastName("111")
				.firstName("111")
				.build();

		user = userRepository.save(user);

		UserEntity check = userRepository.findById(user.getId()).get();

		assert check.getId().equals(user.getId());
	}

}
