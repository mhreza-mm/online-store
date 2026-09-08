package com.store.onlinestore.dto;

import lombok.Data;

@Data
public class ProductRequest {

    private String title;
    private Double price;
    private String image;
    private String type;
    private String brand;
    private Integer stock;


}
