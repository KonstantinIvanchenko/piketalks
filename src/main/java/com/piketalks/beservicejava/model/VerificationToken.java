package com.piketalks.beservicejava.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;
    //@Column(unique = true)
    private String token;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private Instant expiryDate;
}
