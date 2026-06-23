package com.thameem.ecommerce.service.impl;

import com.thameem.ecommerce.dto.AddToCartRequest;
import com.thameem.ecommerce.dto.CartResponse;
import com.thameem.ecommerce.entity.Cart;
import com.thameem.ecommerce.entity.CartItem;
import com.thameem.ecommerce.entity.Product;
import com.thameem.ecommerce.entity.User;
import com.thameem.ecommerce.exception.BadRequestException;
import com.thameem.ecommerce.exception.ResourceNotFoundException;
import com.thameem.ecommerce.repository.CartRepository;
import com.thameem.ecommerce.repository.ProductRepository;
import com.thameem.ecommerce.repository.UserRepository;
import com.thameem.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired private CartRepository cartRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;

    @Override
    public CartResponse getCart(String userEmail) {
        Cart cart = getCartByEmail(userEmail);
        return toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addToCart(String userEmail, AddToCartRequest request) {
        Cart cart = getCartByEmail(userEmail);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", request.getProductId()));

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new BadRequestException("Insufficient stock. Available: " + product.getStockQuantity());
        }

        // If item already in cart, increase quantity
        cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + request.getQuantity()),
                        () -> {
                            CartItem newItem = CartItem.builder()
                                    .cart(cart)
                                    .product(product)
                                    .quantity(request.getQuantity())
                                    .build();
                            cart.getCartItems().add(newItem);
                        }
                );

        return toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse updateCartItem(String userEmail, Long cartItemId, int quantity) {
        Cart cart = getCartByEmail(userEmail);
        CartItem item = cart.getCartItems().stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (quantity <= 0) {
            cart.getCartItems().remove(item);
        } else {
            item.setQuantity(quantity);
        }
        return toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse removeFromCart(String userEmail, Long cartItemId) {
        Cart cart = getCartByEmail(userEmail);
        cart.getCartItems().removeIf(item -> item.getId().equals(cartItemId));
        return toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public void clearCart(String userEmail) {
        Cart cart = getCartByEmail(userEmail);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    // ─── Helpers ─────────────────────────────────────────────

    private Cart getCartByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    private CartResponse toResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setCartId(cart.getId());

        List<CartResponse.CartItemResponse> items = cart.getCartItems().stream().map(item -> {
            CartResponse.CartItemResponse itemResponse = new CartResponse.CartItemResponse();
            itemResponse.setCartItemId(item.getId());
            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setProductName(item.getProduct().getName());
            itemResponse.setProductImageUrl(item.getProduct().getImageUrl());
            itemResponse.setUnitPrice(item.getProduct().getPrice());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setSubtotal(item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity())));
            return itemResponse;
        }).toList();

        response.setItems(items);
        response.setTotalPrice(cart.getTotalPrice());
        return response;
    }
}
