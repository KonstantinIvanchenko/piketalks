package com.piketalks.beservicejava.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.Instant;


@Data
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    private String password;
    @Email
    @NotNull
    private String email;
    private Instant createdDate;
    private boolean enabled;
}

