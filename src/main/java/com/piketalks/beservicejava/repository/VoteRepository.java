package com.piketalks.beservicejava.repository;

import com.piketalks.beservicejava.model.Post;
import com.piketalks.beservicejava.model.User;
import com.piketalks.beservicejava.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User user);
}
