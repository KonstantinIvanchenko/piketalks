package com.piketalks.beservicejava.mapper;

import com.piketalks.beservicejava.dto.CommentDto;
import com.piketalks.beservicejava.dto.PostRequest;
import com.piketalks.beservicejava.model.Comment;
import com.piketalks.beservicejava.model.Post;
import com.piketalks.beservicejava.model.Subtalk;
import com.piketalks.beservicejava.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "text", source = "commentDto.text")
    @Mapping(target = "post", source = "post")
    Comment mapDtoToComment(CommentDto commentDto, Post post, User user);


    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "username", expression = "java(comment.getUser().getUsername())")
    CommentDto mapCommentToDto(Comment comment);
}
