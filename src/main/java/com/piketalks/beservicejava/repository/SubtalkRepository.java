package com.piketalks.beservicejava.repository;

import com.piketalks.beservicejava.model.Subtalk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubtalkRepository extends JpaRepository<Subtalk, Long> {
    Optional<Subtalk> findByName(String subtalkName);
}
