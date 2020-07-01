package com.piketalks.beservicejava.controller;

import com.piketalks.beservicejava.dto.CommentDto;
import com.piketalks.beservicejava.model.Comment;
import com.piketalks.beservicejava.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comments/")
public class CommentsController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentDto commentDto){
        commentService.save(commentDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("by-postId/{postId}")
    public ResponseEntity<List<CommentDto>>  getAllCommentsForPost(@PathVariable Long postId ){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsForPost(postId));
    }

    @PostMapping("by-postId/{userName}")
    public ResponseEntity<List<CommentDto>>  getAllCommentsForPost(@PathVariable String userName ){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsByUsername(userName));
    }

}
