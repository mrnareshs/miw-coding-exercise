package com.miw.gildedrose.service;

import com.miw.gildedrose.config.SurgePriceConfigProperties;
import com.miw.gildedrose.dto.ItemDTO;
import com.miw.gildedrose.dto.OrderDTO;
import com.miw.gildedrose.entity.Item;
import com.miw.gildedrose.entity.Order;
import com.miw.gildedrose.processor.SurgePriceProcessor;
import com.miw.gildedrose.repository.ItemRepository;
import com.miw.gildedrose.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderServiceTest {
    private OrderService orderService;
    private ItemRepository itemRepository;
    private OrderRepository orderRepository;
    
    @BeforeEach
    void setUp() {
        SurgePriceConfigProperties surgePriceConfigProperties = new SurgePriceConfigProperties();
        surgePriceConfigProperties.setSurgeByPercent(10);
        surgePriceConfigProperties.setSurgeInterval(5);
        surgePriceConfigProperties.setSurgeThreshold(5);
        itemRepository = mock(ItemRepository.class);
        orderRepository = mock(OrderRepository.class);
        SurgePriceProcessor surgePriceProcessor = new SurgePriceProcessor(surgePriceConfigProperties);
        orderService = new OrderService(orderRepository, itemRepository, surgePriceProcessor);
    }

    @Test
    void findOrderById() {
        Item item = Item.builder().id(1L).build();
        Order order = Order.builder().id(1L).orderBy("User1").orderQty(5).pricePerUnit(5f).orderQty(5).item(item).build();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        OrderDTO orderDTO = orderService.findOrderById(1L);
        assertNotNull(orderDTO);
        assertEquals(order.getId(), orderDTO.getId());

        boolean exceptionOccured = false;
        ResponseStatusException exceptionReceived = null;
        orderDTO = null;
        try {
            when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
            orderDTO =orderService.findOrderById(1L);
        }catch (ResponseStatusException ex){
            exceptionOccured = true;
            exceptionReceived = ex;
        }
        assertNull(orderDTO);
        assertTrue(exceptionOccured);
        assertTrue(exceptionReceived.getMessage().contains("Order Not Found" ));
    }

    @Test
    void findAllOrders() {
        Item item = Item.builder().id(1L).build();
        List<Order> orders = Arrays.asList(
                Order.builder().id(1L).orderBy("User1").orderQty(5).pricePerUnit(5f).orderQty(5).item(item).build(),
                Order.builder().id(1L).orderBy("User2").orderQty(5).pricePerUnit(5f).orderQty(5).item(item).build()
        );
        when(orderRepository.findAll()).thenReturn(orders);
        List<OrderDTO> orderDTOS = orderService.findAllOrders();
        assertEquals(2, orderDTOS.size());
    }

    @Test
    void saveOrder() {
        OrderDTO orderDTO = OrderDTO.builder().id(1L).orderBy("User1").orderQty(5).pricePerUnit(5f).orderQty(5).itemId(1L).build();
        //when()
    }
}