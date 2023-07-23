package com.example.postgres.user.service;

import com.example.postgres.user.entity.UserEntity;
import com.example.postgres.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserAuthService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);
        if(userEntityOptional.isEmpty()) throw new UsernameNotFoundException("User with email not found");

        UserEntity user = userEntityOptional.get();
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        return new User(user.getEmail(), user.getPassword(), authorities);
    }
}
