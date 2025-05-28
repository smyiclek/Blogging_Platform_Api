package com.example.blogging_platform_api.repository;

import com.example.blogging_platform_api.entity.Author;
import com.example.blogging_platform_api.entity.Comment;
import com.example.blogging_platform_api.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findByPost(Post post);


    List<Comment> findByAuthor(Author author);

}