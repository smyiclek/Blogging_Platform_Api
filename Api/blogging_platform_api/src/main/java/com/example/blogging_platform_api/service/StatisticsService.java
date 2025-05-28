package com.example.blogging_platform_api.service;

import com.example.blogging_platform_api.dto.StatisticsDto;
import com.example.blogging_platform_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatisticsService {

    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public StatisticsService(AuthorRepository authorRepository,
                             PostRepository postRepository,
                             CategoryRepository categoryRepository,
                             TagRepository tagRepository,
                             CommentRepository commentRepository) {
        this.authorRepository = authorRepository;
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true)
    public StatisticsDto getGlobalStatistics() {
        long totalAuthors = authorRepository.count();
        long totalPosts = postRepository.count();
        long totalCategories = categoryRepository.count();
        long totalTags = tagRepository.count();
        long totalComments = commentRepository.count();

        return new StatisticsDto(totalAuthors, totalPosts, totalCategories, totalTags, totalComments);
    }
}