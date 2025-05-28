package com.example.blogging_platform_api.service;

import com.example.blogging_platform_api.dto.CreateTagRequestDto;
import com.example.blogging_platform_api.dto.TagResponseDto;
import com.example.blogging_platform_api.dto.UpdateTagRequestDto;
import com.example.blogging_platform_api.entity.Tag;
import com.example.blogging_platform_api.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    private TagResponseDto convertToDto(Tag tag) {
        TagResponseDto dto = new TagResponseDto();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }

    @Transactional
    public TagResponseDto createTag(CreateTagRequestDto createTagDto) {
        tagRepository.findByNameIgnoreCase(createTagDto.getName()).ifPresent(existingTag -> {
            throw new IllegalArgumentException("Tag with name '" + createTagDto.getName() + "' already exists.");
        });

        Tag tag = new Tag();
        tag.setName(createTagDto.getName());

        Tag savedTag = tagRepository.save(tag);
        return convertToDto(savedTag);
    }

    @Transactional(readOnly = true)
    public TagResponseDto getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + id));
        return convertToDto(tag);
    }

    @Transactional(readOnly = true)
    public List<TagResponseDto> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TagResponseDto updateTag(Long id, UpdateTagRequestDto updateTagDto) {
        Tag tagToUpdate = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + id));

        if (updateTagDto.getName() != null && !updateTagDto.getName().isBlank()) {
            tagRepository.findByNameIgnoreCase(updateTagDto.getName()).ifPresent(existingTag -> {
                if (!existingTag.getId().equals(id)) {
                    throw new IllegalArgumentException("Tag with name '" + updateTagDto.getName() + "' already exists.");
                }
            });
            tagToUpdate.setName(updateTagDto.getName());
        }

        Tag updatedTag = tagRepository.save(tagToUpdate);
        return convertToDto(updatedTag);
    }

    @Transactional
    public void deleteTag(Long id) {
        Tag tagToDelete = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + id));



        if (!tagToDelete.getPosts().isEmpty()) {
            throw new IllegalStateException("Tag with id " + id + " is associated with posts and cannot be deleted. Remove from posts first.");
        }

        tagRepository.deleteById(id);
    }
}