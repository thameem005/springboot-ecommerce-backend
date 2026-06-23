package com.thameem.ecommerce.service;

import com.thameem.ecommerce.dto.OrderResponse;
import com.thameem.ecommerce.dto.PlaceOrderRequest;
import com.thameem.ecommerce.entity.Order;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(String userEmail, PlaceOrderRequest request);
    OrderResponse getOrderById(Long orderId, String userEmail);
    List<OrderResponse> getUserOrders(String userEmail);
    List<OrderResponse> getAllOrders();  // admin only
    OrderResponse updateOrderStatus(Long orderId, Order.OrderStatus status);
}
