package com.miw.gildedrose.service;

import com.miw.gildedrose.config.SurgePriceConfigProperties;
import com.miw.gildedrose.dto.ItemDTO;
import com.miw.gildedrose.entity.Item;
import com.miw.gildedrose.processor.SurgePriceProcessor;
import com.miw.gildedrose.repository.ItemRepository;
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

class ItemServiceTest {
    private ItemService itemService;
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        SurgePriceConfigProperties surgePriceConfigProperties = new SurgePriceConfigProperties();
        surgePriceConfigProperties.setSurgeByPercent(10);
        surgePriceConfigProperties.setSurgeInterval(5);
        surgePriceConfigProperties.setSurgeThreshold(5);
        itemRepository = mock(ItemRepository.class);
        SurgePriceProcessor surgePriceProcessor = new SurgePriceProcessor(surgePriceConfigProperties);
        itemService = new ItemService(itemRepository, surgePriceProcessor);
    }


    @Test
    void findItemById() {
        Item item = Item.builder().id(1L).name("Test1").description("Test Item").price(5f).quantity(10).build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        ItemDTO itemDTO = itemService.findItemById(1L);
        assertNotNull(itemDTO);
        assertEquals(item.getId(), itemDTO.getId());

        boolean exceptionOccured = false;
        ResponseStatusException exceptionReceived = null;
        itemDTO = null;
        try {
            when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
            itemDTO =itemService.findItemById(1L);
        }catch (ResponseStatusException ex){
            exceptionOccured = true;
            exceptionReceived = ex;
        }
        assertNull(itemDTO);
        assertTrue(exceptionOccured);
        assertTrue(exceptionReceived.getMessage().contains("Item Not Found" ));
    }

    @Test
    void findAllItems() {
        List<Item> items = Arrays.asList(
                Item.builder().id(1L).name("Test1").description("Test Item1").price(5f).quantity(10).build(),
                Item.builder().id(2L).name("Test2").description("Test Item2").price(5f).quantity(10).build(),
                Item.builder().id(2L).name("Test3").description("Test Item3").price(5f).quantity(10).build()
        );
        when(itemRepository.findAll()).thenReturn(items);
        List<ItemDTO> itemDTOs = itemService.findAllItems();
        assertEquals(3, itemDTOs.size());
    }

    @Test
    void saveItem() {
        Item item = Item.builder().id(1L).name("Test1").description("Test Item").price(5f).quantity(10).build();
        when(itemRepository.save(item)).thenReturn(item);
        Item itemSaved = itemService.saveItem(item);
        assertEquals(item, itemSaved);
    }
}