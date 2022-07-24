package com.miw.gildedrose.service;

import com.miw.gildedrose.dto.OrderDTO;
import com.miw.gildedrose.entity.Item;
import com.miw.gildedrose.entity.Order;
import com.miw.gildedrose.processor.SurgePriceProcessor;
import com.miw.gildedrose.repository.ItemRepository;
import com.miw.gildedrose.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    private OrderRepository orderRepository;
    private ItemRepository itemRepository;
    private SurgePriceProcessor surgePriceProcessor;

    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository, SurgePriceProcessor surgePriceProcessor){
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.surgePriceProcessor = surgePriceProcessor;
    }

    public OrderDTO findOrderById(Long orderId) {
        log.info("Inside findOrderById of OrderService for orderId: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order Not Found"));
        return this.convertEntityToDTO(order);
    }
    public List<OrderDTO> findAllOrders(){
        log.info("Inside findAllOrders of OrderService");
        List<OrderDTO> orderDTOs = orderRepository.findAll()
                .stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
        return orderDTOs;
    }

    public OrderDTO saveOrder(OrderDTO orderDTO, String orderBy) throws Exception{
        log.info("Inside saveOrder of OrderService");
        orderDTO.setOrderBy(orderBy);
        Item item = itemRepository.findById(orderDTO.getItemId())
                .orElseThrow(()->new HttpClientErrorException(HttpStatus.BAD_REQUEST,"Item to Order does not Exist."));
        if(item.getQuantity()<=0){
            throw new HttpClientErrorException(HttpStatus.PRECONDITION_FAILED,"Item is Out of Stock.");
        }
        if(item.getQuantity()>= orderDTO.getOrderQty()){
            float computeSellPrice = surgePriceProcessor.computeSurgePriceForItemId(item);
            if(orderDTO.getPricePerUnit()!=computeSellPrice){
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"Order Price and Item Price Do not match.");
            }
            item.setQuantity(item.getQuantity()-orderDTO.getOrderQty());
            Order order = this.convertDTOToEntity(orderDTO, item);
            orderRepository.save(order);
            orderDTO = this.convertEntityToDTO(order);
        } else {
            throw new HttpClientErrorException(HttpStatus.PRECONDITION_FAILED,"Item availability is less than Order Quanity.");
        }
        return orderDTO;
    }

    private OrderDTO convertEntityToDTO(Order order){
        OrderDTO orderDTO = OrderDTO.builder()
                    .id(order.getId())
                    .itemId(order.getItem().getId())
                    .orderBy(order.getOrderBy())
                    .orderQty(order.getOrderQty())
                    .pricePerUnit(order.getPricePerUnit())
                    .build();
        return orderDTO;
    }

    private Order convertDTOToEntity(OrderDTO orderDTO, Item item){
        Order order = Order.builder()
                .id(orderDTO.getId())
                .item(item)
                .orderBy(orderDTO.getOrderBy())
                .orderQty(orderDTO.getOrderQty())
                .pricePerUnit(orderDTO.getPricePerUnit())
                .build();
        return order;
    }

}
