package com.farmagro.backend.service;

import com.farmagro.backend.dto.UnitMeasureDTO;
import java.util.List;

public interface UnitMeasureService {
    List<UnitMeasureDTO> findAll();
    UnitMeasureDTO findById(Integer id);
}