package com.thameem.ecommerce.service;

import com.thameem.ecommerce.dto.AddToCartRequest;
import com.thameem.ecommerce.dto.CartResponse;

public interface CartService {
    CartResponse getCart(String userEmail);
    CartResponse addToCart(String userEmail, AddToCartRequest request);
    CartResponse updateCartItem(String userEmail, Long cartItemId, int quantity);
    CartResponse removeFromCart(String userEmail, Long cartItemId);
    void clearCart(String userEmail);
}
