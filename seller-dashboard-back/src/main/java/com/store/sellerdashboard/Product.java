package com.store.sellerdashboard;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="products")
@Data
public class Product {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private String title;
  @Column(nullable=false) private double price;
  private String image;
  private String type;
  private String brand;
  @Column(nullable=false) private Integer stock = 0;
}
