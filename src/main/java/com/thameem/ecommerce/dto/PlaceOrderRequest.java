package com.thameem.ecommerce.dto;

import com.thameem.ecommerce.entity.Order;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlaceOrderRequest {
    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;
}
