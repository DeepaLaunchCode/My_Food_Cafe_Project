package com.myfoodcafe.repository;

import com.myfoodcafe.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderRepository extends JpaRepository<Order, Long> {}
