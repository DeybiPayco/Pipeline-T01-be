package com.farmagro.backend.service.impl;

import com.farmagro.backend.dto.DocumentQueryDTO;
import com.farmagro.backend.exception.BusinessException;
import com.farmagro.backend.service.DocumentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocumentQueryServiceImpl implements DocumentQueryService {

    private static final String GRAPH_PERU_URL = "https://graphperu.daustinn.com/api/query/";

    private final RestTemplate restTemplate;

    @Override
    @SuppressWarnings("unchecked")
    public DocumentQueryDTO queryByDocument(String docNumber) {
        try {
            Map<String, Object> response = restTemplate.getForObject(
                    GRAPH_PERU_URL + docNumber,
                    Map.class
            );

            if (response == null) {
                throw new BusinessException("No se encontraron datos para el documento: " + docNumber);
            }

            return DocumentQueryDTO.builder()
                    .documentId(getString(response, "documentID"))
                    .names(getString(response, "names"))
                    .paternalLastName(getString(response, "paternalLastName"))
                    .maternalLastName(getString(response, "maternalLastName"))
                    .fullName(getString(response, "fullName"))
                    .build();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("No se encontraron datos para el documento: " + docNumber);
        }
    }

    private String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : "";
    }
}
