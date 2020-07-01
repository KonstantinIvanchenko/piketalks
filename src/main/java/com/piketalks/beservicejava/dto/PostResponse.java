package com.piketalks.beservicejava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long postId;
    private String subtalkName;
    private String postName;
    private String username;
    private String url;
    private String description;

    private Integer voteCount;
    private Integer commentCount;
    private String duration;
}
