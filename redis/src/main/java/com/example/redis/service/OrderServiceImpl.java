package com.example.redis.service;

import com.example.redis.domain.Order;
import com.example.redis.exception.OrderNotFoundException;
import com.example.redis.exception.OrderStatusException;
import com.example.redis.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order createOrder(Order order) {

        return orderRepository.save(order);

    }

    @Override
    @Cacheable(value = "Order", key = "#orderId", cacheManager = "testCacheManager") //@Cacheable : DB에서 어플리케이션으로 데이터를 가져오고 Cache에 저장하는데 사용 / DB에 데이터를 가져오는 메서드에 적용
    public Order getOrder(Integer orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order Not Found"));
    }

    @Override
    @CachePut(value = "Order", key = "#orderId", cacheManager = "testCacheManager") //@CachePut : DB의 데이터 업데이트가 있을 때 Redis Cache에 데이터를 업데이트 / DB에서 PUT/PATCH와 같은 업데이트에서 사용
    public Order updateOrder(Order order, Integer orderId) {
        /*
        order status 변화주기
        status: ready -> processing -> shipped -> delivered
         */

        Order orderObject = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order Not Found"));
        if (orderObject.getOrderStatus().equals("ready")) {
            orderObject.setOrderStatus("processing");
        } else if (orderObject.getOrderStatus().equals("processing")) {
            orderObject.setOrderStatus("shipped");
        } else if (orderObject.getOrderStatus().equals("shipped")) {
            orderObject.setOrderStatus("delivered");
        } else {
            throw new OrderStatusException("Order Status Cannot Change");
        }

        return orderRepository.save(orderObject);
    }

    @Override
    @CacheEvict(value = "Order", key = "#orderId", cacheManager = "testCacheManager") //@CacheEvict : DB의 데이터 삭제가 있을 때 Redis Cache에 데이터를 삭제 / DB에서 DELETE와 같은 삭제에서 사용
    public void deleteOrder(Integer orderId) {

        Order orderObject = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order Not Found"));
        orderRepository.delete(orderObject);

    }

    @Override
    @Cacheable(value = "Order", cacheManager = "testCacheManager")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
