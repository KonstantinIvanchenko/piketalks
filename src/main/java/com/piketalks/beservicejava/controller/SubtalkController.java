package com.piketalks.beservicejava.controller;

import com.piketalks.beservicejava.dto.SubtalkDto;
import com.piketalks.beservicejava.service.SubtalkService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subtalk")
@AllArgsConstructor
@Slf4j
public class SubtalkController {

    private final SubtalkService subtalkService;

    @PostMapping
    public ResponseEntity<SubtalkDto> createSubtalk(@RequestBody SubtalkDto subtalkDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subtalkService.save(subtalkDto));
    }

    @GetMapping
    public ResponseEntity<List<SubtalkDto>> getAllSubreddits(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(subtalkService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubtalkDto> getSubtalk(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(subtalkService.getSubtalk(id));
    }
}
