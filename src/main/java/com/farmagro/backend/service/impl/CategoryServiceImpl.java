package com.farmagro.backend.service.impl;

import com.farmagro.backend.dto.CategoryDTO;
import com.farmagro.backend.exception.BusinessException;
import com.farmagro.backend.exception.ResourceNotFoundException;
import com.farmagro.backend.model.Category;
import com.farmagro.backend.repository.CategoryRepository;
import com.farmagro.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAllActive() {
        return categoryRepository.findByStatus(true)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO findById(Integer id) {
        return toDTO(categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la categoría", id)));
    }

    @Override
    @Transactional
    public CategoryDTO create(CategoryDTO dto) {
        if (categoryRepository.existsByNameIgnoreCase(dto.getName()))
            throw new BusinessException("Ya existe una categoría con el nombre: " + dto.getName());

        Category entity = Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .extraData(dto.getExtraData())
                .status(true)
                .createdAt(LocalDateTime.now())
                .build();

        return toDTO(categoryRepository.save(entity));
    }

    @Override
    @Transactional
    public CategoryDTO update(Integer id, CategoryDTO dto) {
        Category entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la categoría", id));

        if (!entity.getName().equalsIgnoreCase(dto.getName())
                && categoryRepository.existsByNameIgnoreCase(dto.getName()))
            throw new BusinessException("Ya existe una categoría con el nombre: " + dto.getName());

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setExtraData(dto.getExtraData());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());

        return toDTO(categoryRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Category entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la categoría", id));

        entity.setStatus(false);
        entity.setDeletedAt(LocalDateTime.now());
        entity.setRestoredAt(null);

        categoryRepository.save(entity);
    }

    @Override
    @Transactional
    public CategoryDTO restore(Integer id) {
        Category entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la categoría", id));

        if (Boolean.TRUE.equals(entity.getStatus()))
            throw new BusinessException("La categoría ya se encuentra activa");

        entity.setStatus(true);
        entity.setRestoredAt(LocalDateTime.now());
        entity.setDeletedAt(null);

        return toDTO(categoryRepository.save(entity));
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private CategoryDTO toDTO(Category e) {
        return CategoryDTO.builder()
                .idCategory(e.getIdCategory())
                .name(e.getName())
                .description(e.getDescription())
                .extraData(e.getExtraData())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .restoredAt(e.getRestoredAt())
                .build();
    }
}