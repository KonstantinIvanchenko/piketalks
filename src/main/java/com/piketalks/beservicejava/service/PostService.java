package com.piketalks.beservicejava.service;

import com.piketalks.beservicejava.dto.PostRequest;
import com.piketalks.beservicejava.dto.PostResponse;
import com.piketalks.beservicejava.dto.SubtalkDto;
import com.piketalks.beservicejava.exceptions.PiketalksException;
import com.piketalks.beservicejava.mapper.PostMapper;
import com.piketalks.beservicejava.model.Post;
import com.piketalks.beservicejava.model.Subtalk;
import com.piketalks.beservicejava.model.User;
import com.piketalks.beservicejava.repository.PostRepository;
import com.piketalks.beservicejava.repository.SubtalkRepository;
import com.piketalks.beservicejava.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final SubtalkRepository subtalkRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public void save(PostRequest postRequest){
        //postRepository.save(postMapper.map(postRequest, postRequest.getSubtalkName(), ));
        Subtalk subtalk = subtalkRepository.findByName(postRequest.getSubtalkName())
        .orElseThrow( () -> new PiketalksException("Subtalk not found exception " + postRequest.getSubtalkName()));

        User currentUser = authService.getCurrentUser();

        postRepository.save(postMapper.map(postRequest, subtalk, currentUser));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PiketalksException("Post not found exception " + id.toString()));

        return postMapper.maptoDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(){

        return postRepository.findAll()
                .stream()
                .map(postMapper::maptoDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubtalk(Long subtalkId){
        Subtalk subtalk = subtalkRepository.findById(subtalkId)
                .orElseThrow(() -> new PiketalksException("No subtalk with id " + subtalkId.toString()));

        return postRepository.findAllBySubtalk(subtalk)
                .stream()
                .map(postMapper::maptoDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username){
        List<User> user = userRepository.findByUsername(username);
        if (user.isEmpty())
            return null;

        return postRepository.findAllByUser(user.get(0))
                .stream()
                .map(postMapper::maptoDto)
                .collect(Collectors.toList());
    }

}
