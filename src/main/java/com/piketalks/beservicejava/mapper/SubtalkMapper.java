package com.piketalks.beservicejava.mapper;
import com.piketalks.beservicejava.dto.SubtalkDto;
import com.piketalks.beservicejava.model.Post;
import com.piketalks.beservicejava.model.Subtalk;
import com.piketalks.beservicejava.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubtalkMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subtalk.getPosts()))")
    SubtalkDto mapSubtalkToDto(Subtalk subtalk);

    default Integer mapPosts(List<Post> listOfPosts){
        return listOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true) //Ignored, as 'posts' must be generated when post is created by user
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    Subtalk maptoToSubtalk(SubtalkDto subtalkDto, User user);
}
