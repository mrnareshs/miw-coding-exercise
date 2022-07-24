package com.miw.gildedrose.controller;

import com.miw.gildedrose.dto.OrderDTO;
import com.miw.gildedrose.service.ItemService;
import com.miw.gildedrose.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    /**
     * List of Orders
     * @return
     */
    @GetMapping
    public List<OrderDTO> listAllOrders() {
        log.info("Inside listAllOrders method of OrderController");
        return orderService.findAllOrders();
    }

    /**
     * Return Order based on id
     * @param orderId
     * @return
     */
    @GetMapping("/{id}")
    public OrderDTO findOrderById(@PathVariable("id") Long orderId) {
        log.info("Inside findOrderById method of OrderController for orderId: {}", orderId);
        return orderService.findOrderById(orderId);
    }

    /**
     * Add Order
     * @param orderDTO
     * @param principal
     * @return
     * @throws Exception
     */
    @PostMapping
    public OrderDTO addOrder(@RequestBody OrderDTO orderDTO, Principal principal) throws Exception {
        log.info("Inside addOrder method  of OrderController");
        return orderService.saveOrder(orderDTO, principal.getName());
    }


}
