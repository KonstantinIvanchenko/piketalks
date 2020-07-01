package com.piketalks.beservicejava.mapper;

import com.piketalks.beservicejava.dto.VoteDto;
import com.piketalks.beservicejava.model.Post;
import com.piketalks.beservicejava.model.User;
import com.piketalks.beservicejava.model.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.persistence.Id;

@Mapper(componentModel = "spring")
public interface VoteMapper {

    @Mapping(target = "voteId", ignore = true)
    @Mapping(target = "voteType", source = "voteDto.voteType")
    @Mapping(target = "post", source = "post")
    Vote mapVoteDtoToVote(VoteDto voteDto, Post post, User user);


    @Mapping(target = "postId", expression = "java(vote.getPost().getPostId())")
    @Mapping(target = "id", source = "voteId")
    VoteDto mapVoteToDto(Vote vote);
}
