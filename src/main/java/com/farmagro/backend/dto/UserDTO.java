package com.farmagro.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Integer idUser;

    // DNI | CE | RUC | PAS
    @NotBlank(message = "El tipo de documento es obligatorio")
    @Pattern(regexp = "^(DNI|CE|RUC|PAS)$",
            message = "El tipo de documento debe ser: DNI, CE, RUC o PAS")
    private String docType;

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 12, message = "El número de documento no puede superar los 12 caracteres")
    private String docNumber;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 150, message = "El apellido no puede superar los 150 caracteres")
    private String lastName;

    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe tener 9 dígitos numéricos")
    private String phone;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    @Size(max = 150)
    private String email;

    // Opcional en respuestas GET (no se expone en listados públicos)
    @Size(max = 255)
    private String passwordHash;

    // ADMIN | VEND | ALMAC | DELIV | CLIENT
    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "^(ADMIN|VEND|ALMAC|DELIV|CLIENT)$",
            message = "El rol debe ser: ADMIN, VEND, ALMAC, DELIV o CLIENT")
    private String role;

    // true = activo, false = inactivo (BIT en BD)
    private Boolean status;

    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime restoredAt;
}
