package com.farmagro.backend.service.impl;

import com.farmagro.backend.dto.UnitMeasureDTO;
import com.farmagro.backend.exception.ResourceNotFoundException;
import com.farmagro.backend.model.UnitMeasure;
import com.farmagro.backend.repository.UnitMeasureRepository;
import com.farmagro.backend.service.UnitMeasureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UnitMeasureServiceImpl implements UnitMeasureService {

    private final UnitMeasureRepository unitMeasureRepository;

    @Override @Transactional(readOnly = true)
    public List<UnitMeasureDTO> findAll() {
        return unitMeasureRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public UnitMeasureDTO findById(Integer id) {
        return toDTO(unitMeasureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la unidad de medida", id)));
    }

    private UnitMeasureDTO toDTO(UnitMeasure e) {
        return UnitMeasureDTO.builder()
                .idUnit(e.getIdUnit())
                .name(e.getName())
                .abbreviation(e.getAbbreviation())
                .build();
    }
}