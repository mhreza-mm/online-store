package com.store.onlinestore.dto;

public class CartItemResponse {

    private Long itemId;
    private Long productId;
    private String title;
    private String image;
    private Double price;
    private Integer quantity;
    private Double subtotal;
    private Integer stock;

    public CartItemResponse() {}

    public CartItemResponse(Long itemId, Long productId,
                            String title, String image,
                            Double price, Integer quantity,
                            Double subtotal, Integer stock) {

        this.itemId = itemId;
        this.productId = productId;
        this.title = title;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.stock = stock;
    }

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
