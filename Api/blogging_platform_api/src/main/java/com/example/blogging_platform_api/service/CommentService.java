package com.example.blogging_platform_api.service;

import com.example.blogging_platform_api.dto.CommentResponseDto;
import com.example.blogging_platform_api.dto.CreateCommentRequestDto;
import com.example.blogging_platform_api.dto.UpdateCommentRequestDto;
import com.example.blogging_platform_api.entity.Author;
import com.example.blogging_platform_api.entity.Comment;
import com.example.blogging_platform_api.entity.Post;
import com.example.blogging_platform_api.repository.AuthorRepository;
import com.example.blogging_platform_api.repository.CommentRepository;
import com.example.blogging_platform_api.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    private final AuthorService authorService;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          AuthorRepository authorRepository,
                          AuthorService authorService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
        this.authorService = authorService;
    }

    private CommentResponseDto convertToDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreationDate(comment.getCreationDate());
        if (comment.getPost() != null) {
            dto.setPostId(comment.getPost().getId());
        }
        if (comment.getAuthor() != null) {
            dto.setAuthor(authorService.getAuthorById(comment.getAuthor().getId()));
        }
        return dto;
    }

    @Transactional
    public CommentResponseDto createComment(CreateCommentRequestDto createCommentDto) {
        Post post = postRepository.findById(createCommentDto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + createCommentDto.getPostId()));
        Author author = authorRepository.findById(createCommentDto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + createCommentDto.getAuthorId()));

        Comment comment = new Comment();
        comment.setContent(createCommentDto.getContent());
        comment.setPost(post);
        comment.setAuthor(author);

        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }

    @Transactional(readOnly = true)
    public CommentResponseDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));
        return convertToDto(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));
        List<Comment> comments = commentRepository.findByPost(post);
        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByAuthorId(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + authorId));
        List<Comment> comments = commentRepository.findByAuthor(author);
        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, UpdateCommentRequestDto updateCommentDto) {
        Comment commentToUpdate = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));

        commentToUpdate.setContent(updateCommentDto.getContent());

        Comment updatedComment = commentRepository.save(commentToUpdate);
        return convertToDto(updatedComment);
    }

    @Transactional
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new EntityNotFoundException("Comment not found with id: " + id);
        }
        commentRepository.deleteById(id);
    }
}