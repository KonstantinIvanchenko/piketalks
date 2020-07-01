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
public class SubtalkDto {
    private Long id;
    private String name;
    private String description;
    private Integer numberOfPosts;
}
