package com.piketalks.beservicejava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    public Long id;
    public String text;
    public Instant createdDate;
    public Long postId;
    public String username;
}
