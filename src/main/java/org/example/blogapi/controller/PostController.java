package org.example.blogapi.controller;

import lombok.val;
import org.example.blogapi.dto.PostResponse;
import org.example.blogapi.entity.PostEntity;
import org.example.blogapi.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@PreAuthorize("hasRole('ADMIN')")
public class PostController {
    @Autowired
    private IPostService service;

    @GetMapping("/")
    public ResponseEntity<PostResponse> listAll(
        @RequestParam(value = "page", defaultValue = "1", required = false) int page,
        @RequestParam(value = "pageSize", defaultValue = "", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue = "", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = "", required = false) String sortDir
    ){
        //Get pageable
        val pageable = service.getPosts(page, pageSize, sortBy, sortDir);

        //Create response
        val response = new PostResponse(
            pageable.getContent(),
            pageable.getNumber(),
            pageable.getTotalElements(),
            pageable.getTotalPages(),
            sortBy,
            sortDir
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    ResponseEntity<PostEntity> getById(@PathVariable("id") Long postId){
        return new ResponseEntity<>(service.getPostById(postId), HttpStatus.OK);
    }

    @PostMapping("/create")
    ResponseEntity<PostEntity> create(@RequestBody PostEntity post){
        return new ResponseEntity<>(service.createPost(post), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    ResponseEntity<PostEntity> update(@RequestBody PostEntity post){
        return new ResponseEntity<>(service.updatePost(post), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> delete(@PathVariable("id") Long postId){
        service.deletePost(postId);

        return new ResponseEntity<>("Post deleted", HttpStatus.OK);
    }
}
