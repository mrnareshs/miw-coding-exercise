package com.miw.gildedrose.service;

import com.miw.gildedrose.dto.ItemDTO;
import com.miw.gildedrose.entity.Item;
import com.miw.gildedrose.processor.SurgePriceProcessor;
import com.miw.gildedrose.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemService {

    private ItemRepository itemRepository;

    private SurgePriceProcessor surgePriceProcessor;

    public ItemService(ItemRepository itemRepository, SurgePriceProcessor surgePriceProcessor){
        this.itemRepository = itemRepository;
        this.surgePriceProcessor = surgePriceProcessor;
    }

    /**
     * Find Item
     * @param itemId
     * @return
     */
    public ItemDTO findItemById(Long itemId) {
        log.info("Inside findItemById of ItemService for ItemId: {}", itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Not Found"));
        ItemDTO itemDTO = this.convertEntityToDTO(item);
        processItemView(itemId);
        return itemDTO;
    }

    /**
     * Find all Items
     * @return
     */
    public List<ItemDTO> findAllItems() {
        log.info("Inside findAllItems of ItemService");
        return itemRepository.findAll()
                .stream()
                .map(t ->{
                    processItemView(t.getId());
                    return t;
                })
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }



    public Item saveItem(Item item) {
        log.info("Inside saveItem of ItemService");
        return itemRepository.save(item);
    }

    private ItemDTO convertEntityToDTO(Item item){
        ItemDTO itemDTO = ItemDTO.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .quantity(item.getQuantity())
                    .price(computeSurgePrice(item))
                    .build();
        return itemDTO;
    }

    private float computeSurgePrice(Item item){
        return surgePriceProcessor.computeSurgePriceForItemId(item);
    }

    private void processItemView(Long id){
       surgePriceProcessor.processItemView(id);
    }

}
