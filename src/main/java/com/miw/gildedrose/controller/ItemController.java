package com.miw.gildedrose.controller;

import com.miw.gildedrose.dto.ItemDTO;
import com.miw.gildedrose.entity.Item;
import com.miw.gildedrose.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    /**
     * Return all Items
     * @return List of Items
     */
    @GetMapping
    public List<ItemDTO> findAllItems() {
        log.info("Inside findAllItems method of ItemController");
        return itemService.findAllItems();
    }

    /**
     * Return the Item based on Id, else exception
     * @param itemId
     * @return
     */
    @GetMapping("/{id}")
    public ItemDTO findItemById(@PathVariable("id") Long itemId) {
        log.info("Inside findItemById method of ItemController for itemId: {}", itemId);
        return itemService.findItemById(itemId);
    }

    /**
     * Save the Item
     * @param item
     * @return
     */
    @PostMapping
    public Item saveItem(@RequestBody @Valid Item item){
        log.info("Inside saveItem method  of ItemController");
        return itemService.saveItem(item);
    }

}
