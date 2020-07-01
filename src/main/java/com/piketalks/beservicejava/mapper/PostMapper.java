package com.piketalks.beservicejava.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.piketalks.beservicejava.dto.PostRequest;
import com.piketalks.beservicejava.dto.PostResponse;
import com.piketalks.beservicejava.model.Post;
import com.piketalks.beservicejava.model.Subtalk;
import com.piketalks.beservicejava.model.User;
import com.piketalks.beservicejava.repository.CommentRepository;
import com.piketalks.beservicejava.repository.VoteRepository;
import com.piketalks.beservicejava.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "subtalk", source = "subtalk") //same fields in postrequest and post
    //@Mapping(target = "user", source = "user") same fields in postrequest and post
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "voteCount", constant = "0")
    public abstract Post map(PostRequest postRequest, Subtalk subtalk, User user);

    //@Mapping(target = "postName", source = "postName") same fields in post and postresponse
    @Mapping(target = "subtalkName", source = "subtalk.name")// same fields in post and postresponse
    @Mapping(target = "username", source = "user.username")// same fields in post and postresponse
    //@Mapping(target = "url", source = "url") same fields in post and postresponse
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse maptoDto(Post post);

    Integer commentCount(Post post){ return commentRepository.findAllByPost(post).size();}
    String getDuration(Post post){return TimeAgo.using(post.getCreatedDate().toEpochMilli());}
}
