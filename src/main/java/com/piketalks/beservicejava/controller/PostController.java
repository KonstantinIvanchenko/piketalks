package com.piketalks.beservicejava.controller;

import com.piketalks.beservicejava.dto.PostRequest;
import com.piketalks.beservicejava.dto.PostResponse;
import com.piketalks.beservicejava.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest){
        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("/by-subtalk/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubtalk(Long id){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsBySubtalk(id));
    }

    @GetMapping("/by-subtalk/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByName(String name){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsByUsername(name));
    }

}
