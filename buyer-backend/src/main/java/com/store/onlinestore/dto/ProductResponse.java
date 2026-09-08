package com.store.onlinestore.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String title;
    private Double price;
    private String image;
    private String type;
    private String brand;
    private Integer stock;
}
