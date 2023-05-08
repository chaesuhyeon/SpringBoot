package com.example.redis.service;

import com.example.redis.domain.Order;

import java.util.List;

public interface OrderService {
    public Order createOrder(Order order);

    public Order getOrder(Integer orderId);

    public Order updateOrder(Order order, Integer orderId);

    public void deleteOrder(Integer orderId);

    public List<Order> getAllOrders();
}
