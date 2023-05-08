package com.example.redis.controller;

import com.example.redis.domain.Order;
import com.example.redis.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping()
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Integer id) {
        return orderService.getOrder(id);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Integer id, @RequestBody Order order) {
        return orderService.updateOrder(order, id);
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);

        return "Order with id: " + id + " deleted.";
    }
}

/**
 * 실행해보기
 * 1. post를 할 때 작성한 Order 객체의 형태대로 작성을 해준다.
 * 2. 생성한 객체를 조회할 경우에 application.yml의 설정에 따라서 쿼리가 찍힌다.
 * 3. 이 때, GET 요청을 많이 날려보았을 때 쿼리가 Request 한 것 만큼 찍히지 않는 것이 보인다면 Redis Cache기능을 사용하고 있는 것을 확인 할 수 있다.
 */