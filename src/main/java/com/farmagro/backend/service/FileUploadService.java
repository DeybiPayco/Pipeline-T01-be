package com.farmagro.backend.service;

import com.farmagro.backend.exception.BusinessException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private static final List<String> ALLOWED_TYPES = List.of(
            "image/jpeg", "image/jpg", "image/png", "image/webp", "image/gif"
    );

    // Crear la carpeta de subida si no existe al arrancar
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear la carpeta de imágenes: " + uploadDir, e);
        }
    }

    /**
     * Guarda el archivo en la carpeta de productos y devuelve la URL relativa.
     * Ejemplo de retorno: /images/products/abc123.jpg
     */
    public String saveProductImage(MultipartFile file) {
        // Validar que sea una imagen
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            throw new BusinessException("Tipo de archivo no permitido. Solo se aceptan: JPG, PNG, WEBP, GIF");
        }

        // Validar tamaño (5 MB máximo)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BusinessException("El archivo supera el tamaño máximo permitido de 5 MB");
        }

        // Generar nombre único para evitar colisiones
        String originalName = file.getOriginalFilename() != null
                ? file.getOriginalFilename() : "image";
        String extension = originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf("."))
                : ".jpg";
        String fileName = UUID.randomUUID().toString() + extension;

        // Guardar el archivo
        try {
            Path destination = Paths.get(uploadDir).resolve(fileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new BusinessException("Error al guardar la imagen: " + e.getMessage());
        }

        return "/images/products/" + fileName;
    }

    /**
     * Elimina una imagen anterior si existe (para reemplazos).
     */
    public void deleteProductImage(String imageUrl) {
        if (imageUrl == null || !imageUrl.startsWith("/images/products/")) return;
        try {
            String fileName = imageUrl.replace("/images/products/", "");
            Path file = Paths.get(uploadDir).resolve(fileName);
            Files.deleteIfExists(file);
        } catch (IOException ignored) {
            // No es crítico si no se puede borrar
        }
    }
}
