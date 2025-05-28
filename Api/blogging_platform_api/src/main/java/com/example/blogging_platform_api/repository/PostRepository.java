package com.example.blogging_platform_api.repository;

import com.example.blogging_platform_api.entity.Author;
import com.example.blogging_platform_api.entity.Category;
import com.example.blogging_platform_api.entity.Post;
import com.example.blogging_platform_api.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAuthor(Author author);

    List<Post> findByCategory(Category category);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t = :tag")
    List<Post> findByTag(@Param("tag") Tag tag);
}