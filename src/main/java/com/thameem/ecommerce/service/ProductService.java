package com.thameem.ecommerce.service;

import com.thameem.ecommerce.dto.ProductRequest;
import com.thameem.ecommerce.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(Long id);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getProductsByCategory(String category);
    List<ProductResponse> searchProducts(String keyword);
    List<String> getAllCategories();
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
}
