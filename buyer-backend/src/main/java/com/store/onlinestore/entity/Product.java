package com.store.onlinestore.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("title")

    private String title;

    @Column(nullable = false)
    private double price;

    @Column(name = "image")
    @JsonProperty("image")
    private String image;

    @Column(name = "type")
    private String type;

    @Column(name = "brand")
    private String brand;

    @Column(nullable = false)
    private Integer stock = 0;

}
