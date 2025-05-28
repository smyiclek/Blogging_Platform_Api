package com.example.blogging_platform_api.controller;

import com.example.blogging_platform_api.dto.CommentResponseDto;
import com.example.blogging_platform_api.dto.CreateCommentRequestDto;
import com.example.blogging_platform_api.dto.UpdateCommentRequestDto;
import com.example.blogging_platform_api.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@Valid @RequestBody CreateCommentRequestDto createCommentDto) {
        CommentResponseDto createdComment = commentService.createComment(createCommentDto);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable Long id) {
        CommentResponseDto comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentResponseDto> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByAuthorId(@PathVariable Long authorId) {
        List<CommentResponseDto> comments = commentService.getCommentsByAuthorId(authorId);
        return ResponseEntity.ok(comments);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id, @Valid @RequestBody UpdateCommentRequestDto updateCommentDto) {
        CommentResponseDto updatedComment = commentService.updateComment(id, updateCommentDto);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}