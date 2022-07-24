package com.miw.gildedrose.repository;

import com.miw.gildedrose.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
