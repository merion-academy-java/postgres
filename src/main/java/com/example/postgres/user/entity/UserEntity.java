package com.example.postgres.user.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Entity
@NoArgsConstructor
@Table(name = "users")
public class UserEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @Column(name = "first_name")
    protected String firstName;
    protected String lastName;
}
