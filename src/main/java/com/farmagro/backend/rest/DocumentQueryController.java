package com.farmagro.backend.rest;

import com.farmagro.backend.dto.DocumentQueryDTO;
import com.farmagro.backend.service.DocumentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/document-query")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DocumentQueryController {

    private final DocumentQueryService documentQueryService;

    @GetMapping("/{docNumber}")
    public ResponseEntity<DocumentQueryDTO> queryDocument(@PathVariable String docNumber) {
        return ResponseEntity.ok(documentQueryService.queryByDocument(docNumber));
    }
}
