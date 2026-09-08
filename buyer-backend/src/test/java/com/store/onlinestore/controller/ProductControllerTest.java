package com.store.onlinestore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.onlinestore.entity.Product;
import com.store.onlinestore.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product savedProduct;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        Product product = Product.builder()
                .title("Laptop")
                .price(1500.0)
                .image("laptop.jpg")
                .build();
        savedProduct = productRepository.save(product);
    }

    @Test
    void getAllProducts_ShouldReturnListFromDatabase() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Laptop")));
    }

    @Test
    void getProductById_ShouldReturnCorrectProduct() throws Exception {
        mockMvc.perform(get("/api/products/{id}", savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Laptop")));
    }

    @Test
    void createProduct_ShouldInsertIntoDatabase() throws Exception {
        Product newProduct = Product.builder()
                .title("Phone")
                .price(800.0)
                .image("phone.jpg")
                .build();

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Phone")));

        List<Product> products = productRepository.findAll();

        assert products.size() == 2;
    }

    @Test
    void updateProduct_ShouldModifyInDatabase() throws Exception {
        Product updatedData = Product.builder()
                .title("Updated Laptop")
                .price(2000.0)
                .image("updated.jpg")
                .build();

        mockMvc.perform(put("/api/products/{id}", savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Laptop")))
                .andExpect(jsonPath("$.price", is(2000.0)));
    }

    @Test
    void deleteProduct_ShouldRemoveFromDatabase() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", savedProduct.getId()))
                .andExpect(status().isOk());

        assert productRepository.count() == 0;
    }
}
