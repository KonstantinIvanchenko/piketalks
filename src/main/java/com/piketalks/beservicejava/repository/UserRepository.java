package com.piketalks.beservicejava.repository;

import com.piketalks.beservicejava.model.User;
import com.piketalks.beservicejava.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(String Username);
    //Optional<User> findByUserName(String UserName);
}
