package com.piketalks.beservicejava.service;

import com.piketalks.beservicejava.dto.VoteDto;
import com.piketalks.beservicejava.exceptions.PiketalksException;
import com.piketalks.beservicejava.mapper.VoteMapper;
import com.piketalks.beservicejava.model.*;
import com.piketalks.beservicejava.repository.PostRepository;
import com.piketalks.beservicejava.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final VoteMapper voteMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void vote(VoteDto voteDto){

        Post post = postRepository.findById(voteDto.postId)
                .orElseThrow(() -> new PiketalksException("Post not found exception: id " + voteDto.postId.toString()));

        User currentUser = authService.getCurrentUser();

        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, currentUser);
        if(voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.voteType)){
            throw new PiketalksException("You have already "+ voteByPostAndUser.get().getVoteType()
                    + "ed post with id" + post.getPostId().toString());
        }

        if (voteDto.voteType.equals(VoteType.UPVOTE))
            post.setVoteCount(post.getVoteCount() + 1);
        if (voteDto.voteType.equals(VoteType.DOWNVOTE))
            post.setVoteCount(post.getVoteCount() - 1);

        postRepository.save(post);
        voteRepository.save(voteMapper.mapVoteDtoToVote(voteDto, post, currentUser));

        String message = mailContentBuilder.build(currentUser + " voted on your post here: " + post.getUrl());
        sendNotificationEmail(message, post.getUser(), currentUser);
    }

    private void sendNotificationEmail(String message, User userTo, User userFrom){
        mailService.sendEmail(new NotificationEmail(userTo.getEmail(),
                userFrom.getUsername() + " voted on your post",
                message));
    }

}

