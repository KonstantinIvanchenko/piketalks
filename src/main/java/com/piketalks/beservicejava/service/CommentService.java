package com.piketalks.beservicejava.service;


import com.piketalks.beservicejava.dto.CommentDto;
import com.piketalks.beservicejava.exceptions.PiketalksException;
import com.piketalks.beservicejava.mapper.CommentMapper;
import com.piketalks.beservicejava.model.*;
import com.piketalks.beservicejava.repository.CommentRepository;
import com.piketalks.beservicejava.repository.PostRepository;
import com.piketalks.beservicejava.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentDto commentDto){
        Post post = postRepository.findById(commentDto.postId)
                .orElseThrow(() -> new PiketalksException("Post not found exception: id " + commentDto.postId.toString()));

        User currentUser = authService.getCurrentUser();

        commentRepository.save(commentMapper.mapDtoToComment(commentDto, post, currentUser));

        String message = mailContentBuilder.build(currentUser + " posted a comment on your post here: " + post.getUrl());
        sendNotificationEmail(message, post.getUser(), currentUser);
    }

    private void sendNotificationEmail(String message, User userTo, User userFrom){
        mailService.sendEmail(new NotificationEmail(userTo.getEmail(),
                userFrom.getUsername() + " send a comment",
                message));
    }

    public List<CommentDto> getAllCommentsForPost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PiketalksException("Post not found exception: id " + postId));

        return commentRepository.findAllByPost(post)
                .stream()
                .map(commentMapper::mapCommentToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllCommentsByUsername(String username){
        List<User> user = userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new PiketalksException("User not found exception: user " + username);

        return commentRepository.findAllByUser(user.get(0))
                .stream()
                .map(commentMapper::mapCommentToDto)
                .collect(Collectors.toList());
    }



}
