package com.farmagro.backend.rest;

import com.farmagro.backend.model.Ubigeo;
import com.farmagro.backend.repository.UbigeoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ubigeos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UbigeoController {

    private final UbigeoRepository ubigeoRepository;

    @GetMapping
    public ResponseEntity<List<Ubigeo>> getAll() {
        return ResponseEntity.ok(ubigeoRepository.findAll());
    }
}
