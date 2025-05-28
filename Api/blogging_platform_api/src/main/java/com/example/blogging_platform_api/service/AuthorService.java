package com.example.blogging_platform_api.service;

import com.example.blogging_platform_api.dto.AuthorResponseDto;
import com.example.blogging_platform_api.dto.CreateAuthorRequestDto;
import com.example.blogging_platform_api.dto.UpdateAuthorRequestDto;
import com.example.blogging_platform_api.entity.Author;
import com.example.blogging_platform_api.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    private AuthorResponseDto convertToDto(Author author) {
        AuthorResponseDto dto = new AuthorResponseDto();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setEmail(author.getEmail());
        dto.setRegistrationDate(author.getRegistrationDate());
        return dto;
    }

    @Transactional
    public AuthorResponseDto createAuthor(CreateAuthorRequestDto createAuthorDto) {
        if (authorRepository.findByEmail(createAuthorDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Author with email " + createAuthorDto.getEmail() + " already exists.");
        }

        Author author = new Author();
        author.setName(createAuthorDto.getName());
        author.setEmail(createAuthorDto.getEmail());

        Author savedAuthor = authorRepository.save(author);
        return convertToDto(savedAuthor);
    }

    @Transactional(readOnly = true)
    public AuthorResponseDto getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
        return convertToDto(author);
    }

    @Transactional(readOnly = true)
    public List<AuthorResponseDto> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AuthorResponseDto updateAuthor(Long id, UpdateAuthorRequestDto updateAuthorDto) {
        Author authorToUpdate = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));

        if (updateAuthorDto.getName() != null && !updateAuthorDto.getName().isBlank()) {
            authorToUpdate.setName(updateAuthorDto.getName());
        }

        if (updateAuthorDto.getEmail() != null && !updateAuthorDto.getEmail().isBlank()) {
            if (authorRepository.findByEmail(updateAuthorDto.getEmail()).isPresent() &&
                    !authorRepository.findByEmail(updateAuthorDto.getEmail()).get().getId().equals(id)) {
                throw new IllegalArgumentException("Email " + updateAuthorDto.getEmail() + " is already in use by another author.");
            }
            authorToUpdate.setEmail(updateAuthorDto.getEmail());
        }

        Author updatedAuthor = authorRepository.save(authorToUpdate);
        return convertToDto(updatedAuthor);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new EntityNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }
}