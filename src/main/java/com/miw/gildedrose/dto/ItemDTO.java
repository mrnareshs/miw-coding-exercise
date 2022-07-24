package com.miw.gildedrose.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
public class ItemDTO {
    private Long id;
    @Min(value=1, message = "Name is required.")
    private String name;
    private String description;
    @NotNull(message = "Price is Required.")
    private float price;
    private int quantity;
}
