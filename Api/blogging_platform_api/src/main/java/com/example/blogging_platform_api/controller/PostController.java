package com.example.blogging_platform_api.controller;

import com.example.blogging_platform_api.dto.CreatePostRequestDto;
import com.example.blogging_platform_api.dto.PostResponseDto;
import com.example.blogging_platform_api.dto.PostStatusUpdateRequestDto;
import com.example.blogging_platform_api.dto.UpdatePostRequestDto;
import com.example.blogging_platform_api.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody CreatePostRequestDto createPostDto) {
        PostResponseDto createdPost = postService.createPost(createPostDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        PostResponseDto post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<PostResponseDto>> getPostsByAuthorId(@PathVariable Long authorId) {
        List<PostResponseDto> posts = postService.getPostsByAuthorId(authorId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PostResponseDto>> getPostsByCategoryId(@PathVariable Long categoryId) {
        List<PostResponseDto> posts = postService.getPostsByCategoryId(categoryId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/tag/{tagId}")
    public ResponseEntity<List<PostResponseDto>> getPostsByTagId(@PathVariable Long tagId) {
        List<PostResponseDto> posts = postService.getPostsByTagId(tagId);
        return ResponseEntity.ok(posts);
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<PostResponseDto> updatePublishStatus(
            @PathVariable Long id,
            @Valid @RequestBody PostStatusUpdateRequestDto statusDto) {
        PostResponseDto updatedPost = postService.updatePostPublicationStatus(id, statusDto.getStatus());
        return ResponseEntity.ok(updatedPost);
    }

    @PatchMapping("/{id}/feature")
    public ResponseEntity<PostResponseDto> updateFeatureStatus(
            @PathVariable Long id,
            @Valid @RequestBody PostStatusUpdateRequestDto statusDto) {
        PostResponseDto updatedPost = postService.updatePostFeaturedStatus(id, statusDto.getStatus());
        return ResponseEntity.ok(updatedPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @Valid @RequestBody UpdatePostRequestDto updatePostDto) {
        PostResponseDto updatedPost = postService.updatePost(id, updatePostDto);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}