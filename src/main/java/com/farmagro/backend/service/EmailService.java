package com.farmagro.backend.service;

import com.farmagro.backend.model.ProductPresentation;
import java.util.List;

public interface EmailService {
    void sendLowStockAlert(List<ProductPresentation> lowStockItems);
}
