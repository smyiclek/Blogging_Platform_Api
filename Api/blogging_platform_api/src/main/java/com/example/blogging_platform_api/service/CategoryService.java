package com.example.blogging_platform_api.service;

import com.example.blogging_platform_api.dto.CategoryResponseDto;
import com.example.blogging_platform_api.dto.CreateCategoryRequestDto;
import com.example.blogging_platform_api.dto.UpdateCategoryRequestDto;
import com.example.blogging_platform_api.entity.Category;
import com.example.blogging_platform_api.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private CategoryResponseDto convertToDto(Category category) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }

    @Transactional
    public CategoryResponseDto createCategory(CreateCategoryRequestDto createCategoryDto) {
        categoryRepository.findByNameIgnoreCase(createCategoryDto.getName()).ifPresent(existingCategory -> {
            throw new IllegalArgumentException("Category with name '" + createCategoryDto.getName() + "' already exists.");
        });

        Category category = new Category();
        category.setName(createCategoryDto.getName());
        category.setDescription(createCategoryDto.getDescription());

        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        return convertToDto(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponseDto updateCategory(Long id, UpdateCategoryRequestDto updateCategoryDto) {
        Category categoryToUpdate = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        if (updateCategoryDto.getName() != null && !updateCategoryDto.getName().isBlank()) {
            categoryRepository.findByNameIgnoreCase(updateCategoryDto.getName()).ifPresent(existingCategory -> {
                if (!existingCategory.getId().equals(id)) {
                    throw new IllegalArgumentException("Category with name '" + updateCategoryDto.getName() + "' already exists.");
                }
            });
            categoryToUpdate.setName(updateCategoryDto.getName());
        }

        if (updateCategoryDto.getDescription() != null) {
            categoryToUpdate.setDescription(updateCategoryDto.getDescription());
        }

        Category updatedCategory = categoryRepository.save(categoryToUpdate);
        return convertToDto(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}