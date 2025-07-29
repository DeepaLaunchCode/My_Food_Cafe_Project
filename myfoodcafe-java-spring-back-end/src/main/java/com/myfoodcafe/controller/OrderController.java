package com.myfoodcafe.controller;

import com.myfoodcafe.dto.GenericResponse;
import com.myfoodcafe.dto.OrderRequest;
import com.myfoodcafe.entity.Order;
import com.myfoodcafe.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Endpoints for managing customer orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<GenericResponse> placeOrder(@Valid @RequestBody OrderRequest orderRequest) {
        Order newOrder = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(new GenericResponse(true, "Order placed successfully!", newOrder.getId()));
    }
}