package com.farmagro.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * RegisterClientDTO — payload para el registro público desde la web.
 *
 * Una sola petición POST /auth/register crea en una transacción:
 *   1. Un registro en USERS  con role = 'CLIENT'
 *   2. Un registro en CUSTOMERS con street + ubigeoId
 *
 * La respuesta devuelve un CustomerDTO con todos los datos del perfil.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterClientDTO {

    // ── Datos personales → van a USERS ───────────────────────────────────────

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Pattern(regexp = "^(DNI|CE|RUC|PAS)$",
            message = "El tipo de documento debe ser: DNI, CE, RUC o PAS")
    private String docType;

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 12, message = "El número de documento no puede superar los 12 caracteres")
    private String docNumber;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150)
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 150)
    private String lastName;

    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe tener 9 dígitos numéricos")
    private String phone;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 255, message = "La contraseña debe tener entre 6 y 255 caracteres")
    private String password;

    // ── Datos de ubicación → van a CUSTOMERS ─────────────────────────────────

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 400)
    private String street;

    @NotNull(message = "El ubigeo es obligatorio")
    private Integer ubigeoId;
}
