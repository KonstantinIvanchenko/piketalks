package com.piketalks.beservicejava.repository;

import com.piketalks.beservicejava.model.Post;
import com.piketalks.beservicejava.model.Subtalk;
import com.piketalks.beservicejava.model.User;
import org.graalvm.compiler.nodes.calc.IntegerDivRemNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubtalk(Subtalk subtalk);
    List<Post> findAllByUser(User user);

    Optional<Post> findByPostName(String postName);
}
