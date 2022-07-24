package com.miw.gildedrose.processor;

import com.miw.gildedrose.config.SurgePriceConfigProperties;
import com.miw.gildedrose.entity.Item;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SurgePriceProcessor  {

    SurgePriceConfigProperties surgePriceConfig;

    public SurgePriceProcessor(SurgePriceConfigProperties surgePriceConfig){
        this.surgePriceConfig = surgePriceConfig;
    }
    private Map<Long, List<LocalDateTime>> viewPerItemTracker = new ConcurrentHashMap<>();

    /**
     * Compute Surge Price For Item
     * @param item
     * @return
     */
    public float computeSurgePriceForItemId(Item item) {
        if(viewPerItemTracker.containsKey(item.getId())){
            List<LocalDateTime> itemViewTime = viewPerItemTracker.get(item.getId());
            if (itemViewTime.size() >= surgePriceConfig.getSurgeThreshold()) {
                return item.getPrice() * (1+(surgePriceConfig.getSurgeByPercent()/100));
            }
        }
        return item.getPrice();
    }

    /**
     * Process Item View
     * @param itemId
     */
    public void processItemView(Long itemId) {
        LocalDateTime now = LocalDateTime.now();
        if(viewPerItemTracker.containsKey(itemId)){
            List<LocalDateTime> itemViewTime = viewPerItemTracker.get(itemId);
            itemViewTime.add(now);
            LocalDateTime surgeWindowStart = now.minusSeconds(surgePriceConfig.getSurgeInterval());
            itemViewTime.removeIf(previous -> previous.isBefore(surgeWindowStart));
        } else {
            List<LocalDateTime> itemViewTime = new ArrayList<>();
            itemViewTime.add(now);
            viewPerItemTracker.put(itemId, itemViewTime);
        }
    }
}

