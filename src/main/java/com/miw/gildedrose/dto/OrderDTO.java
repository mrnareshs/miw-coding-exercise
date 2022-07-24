package com.miw.gildedrose.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class OrderDTO {
    private Long id;
    @Min(value=1, message = "Item is required.")
    private Long itemId;
    private String orderBy;
    @NotNull(message = "Price Not Valid")
    private Float pricePerUnit;
    @Min(value=1, message = "Quantity should be greater than 0")
    private Integer orderQty;

}
