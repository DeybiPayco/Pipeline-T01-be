package com.farmagro.backend.rest;

import com.farmagro.backend.dto.UnitMeasureDTO;
import com.farmagro.backend.service.UnitMeasureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/unit-measures")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UnitMeasureController {

    private final UnitMeasureService unitMeasureService;

    @GetMapping
    public ResponseEntity<List<UnitMeasureDTO>> getAll() {
        return ResponseEntity.ok(unitMeasureService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitMeasureDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(unitMeasureService.findById(id));
    }
}