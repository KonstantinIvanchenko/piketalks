package com.piketalks.beservicejava.repository;

import com.piketalks.beservicejava.model.Comment;
import com.piketalks.beservicejava.model.Post;
import com.piketalks.beservicejava.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    List<Comment>  findAllByUser(User user);
}
