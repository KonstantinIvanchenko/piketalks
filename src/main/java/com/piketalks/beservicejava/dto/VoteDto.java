package com.piketalks.beservicejava.dto;

import com.piketalks.beservicejava.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    public Long id;
    public Long postId;
    public VoteType voteType;
}
