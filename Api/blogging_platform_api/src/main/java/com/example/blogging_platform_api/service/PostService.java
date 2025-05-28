package com.example.blogging_platform_api.service;

import com.example.blogging_platform_api.dto.AuthorResponseDto;
import com.example.blogging_platform_api.dto.CategoryResponseDto;
import com.example.blogging_platform_api.dto.CreatePostRequestDto;
import com.example.blogging_platform_api.dto.PostResponseDto;
import com.example.blogging_platform_api.dto.PostStatusUpdateRequestDto;
import com.example.blogging_platform_api.dto.TagResponseDto;
import com.example.blogging_platform_api.dto.UpdatePostRequestDto;
import com.example.blogging_platform_api.entity.Author;
import com.example.blogging_platform_api.entity.Category;
import com.example.blogging_platform_api.entity.Post;
import com.example.blogging_platform_api.entity.Tag;
import com.example.blogging_platform_api.repository.AuthorRepository;
import com.example.blogging_platform_api.repository.CategoryRepository;
import com.example.blogging_platform_api.repository.PostRepository;
import com.example.blogging_platform_api.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Autowired
    public PostService(PostRepository postRepository,
                       AuthorRepository authorRepository,
                       CategoryRepository categoryRepository,
                       TagRepository tagRepository,
                       AuthorService authorService,
                       CategoryService categoryService,
                       TagService tagService) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    private PostResponseDto convertToDto(Post post) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setPublicationDate(post.getPublicationDate());
        dto.setLastUpdatedDate(post.getLastUpdatedDate());
        dto.setPublished(post.isPublished());
        dto.setFeatured(post.isFeatured());

        if (post.getAuthor() != null) {
            dto.setAuthor(authorService.getAuthorById(post.getAuthor().getId()));
        }

        if (post.getCategory() != null) {
            dto.setCategory(categoryService.getCategoryById(post.getCategory().getId()));
        }

        if (post.getTags() != null && !post.getTags().isEmpty()) {
            dto.setTags(post.getTags().stream()
                    .map(tag -> tagService.getTagById(tag.getId()))
                    .collect(Collectors.toSet()));
        } else {
            dto.setTags(new HashSet<>());
        }
        return dto;
    }

    @Transactional
    public PostResponseDto createPost(CreatePostRequestDto createPostDto) {
        Author author = authorRepository.findById(createPostDto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + createPostDto.getAuthorId()));

        Category category = null;
        if (createPostDto.getCategoryId() != null) {
            category = categoryRepository.findById(createPostDto.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + createPostDto.getCategoryId()));
        }

        Set<Tag> tags = new HashSet<>();
        if (createPostDto.getTagIds() != null && !createPostDto.getTagIds().isEmpty()) {
            List<Tag> foundTags = tagRepository.findAllById(createPostDto.getTagIds());
            if (foundTags.size() != createPostDto.getTagIds().size()) {
                throw new EntityNotFoundException("One or more tags not found for the provided IDs.");
            }
            tags.addAll(foundTags);
        }

        Post post = new Post();
        post.setTitle(createPostDto.getTitle());
        post.setContent(createPostDto.getContent());
        post.setAuthor(author);
        if (category != null) {
            post.setCategory(category);
        }
        post.setTags(tags);

        Post savedPost = postRepository.save(post);
        return convertToDto(savedPost);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
        return convertToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostsByAuthorId(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + authorId));
        List<Post> posts = postRepository.findByAuthor(author);
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostsByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));
        List<Post> posts = postRepository.findByCategory(category);
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostsByTagId(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + tagId));
        List<Post> posts = postRepository.findByTag(tag);
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponseDto updatePostPublicationStatus(Long id, boolean isPublished) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
        post.setPublished(isPublished);
        Post updatedPost = postRepository.save(post);
        return convertToDto(updatedPost);
    }

    @Transactional
    public PostResponseDto updatePostFeaturedStatus(Long id, boolean isFeatured) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
        post.setFeatured(isFeatured);
        Post updatedPost = postRepository.save(post);
        return convertToDto(updatedPost);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, UpdatePostRequestDto updatePostDto) {
        Post postToUpdate = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));

        if (updatePostDto.getTitle() != null && !updatePostDto.getTitle().isBlank()) {
            postToUpdate.setTitle(updatePostDto.getTitle());
        }

        if (updatePostDto.getContent() != null && !updatePostDto.getContent().isBlank()) {
            postToUpdate.setContent(updatePostDto.getContent());
        }

        Post updatedPost = postRepository.save(postToUpdate);
        return convertToDto(updatedPost);
    }

    @Transactional
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new EntityNotFoundException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }
}