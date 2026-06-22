package com.farmagro.backend.service.impl;

import com.farmagro.backend.model.ProductPresentation;
import com.farmagro.backend.model.User;
import com.farmagro.backend.repository.UserRepository;
import com.farmagro.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @Override
    public void sendLowStockAlert(List<ProductPresentation> lowStockItems) {
        // Obtener todos los administradores activos
        List<User> admins = userRepository.findByStatus(true).stream()
                .filter(u -> "ADMIN".equals(u.getRole()))
                .collect(Collectors.toList());

        if (admins.isEmpty() || lowStockItems.isEmpty()) return;

        // Construir el cuerpo del correo
        StringBuilder body = new StringBuilder();
        body.append("⚠️ ALERTA DE STOCK BAJO - FARMAGRO S.A.\n\n");
        body.append("Los siguientes productos tienen stock igual o menor a 5 unidades:\n\n");

        for (ProductPresentation pp : lowStockItems) {
            String productName = pp.getProduct() != null ? pp.getProduct().getName() : "Producto desconocido";
            body.append("• ").append(productName)
                .append(" — ").append(pp.getLabel())
                .append(" | Stock actual: ").append(pp.getCurrentStock()).append(" unidades\n");
        }

        body.append("\nPor favor, realice una orden de reposición a la brevedad.");
        body.append("\n\n— Sistema FARMAGRO");

        // Enviar correo a cada administrador
        for (User admin : admins) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(admin.getEmail());
                message.setSubject("⚠️ Alerta de Stock Bajo - FARMAGRO");
                message.setText(body.toString());
                mailSender.send(message);
                log.info("Alerta de stock enviada a: {}", admin.getEmail());
            } catch (Exception e) {
                log.error("Error al enviar correo a {}: {}", admin.getEmail(), e.getMessage());
            }
        }
    }
}
