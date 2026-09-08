package com.store.onlinestore.controller;

import com.store.onlinestore.dto.PaginationResponse;
import com.store.onlinestore.dto.ProductRequest;
import com.store.onlinestore.dto.ProductResponse;
import com.store.onlinestore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/paginated")
    public PaginationResponse<ProductResponse> getProductsPaginated(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> productPage =
                productService.getProducts(type, brand, search, pageable);

        return new PaginationResponse<>(
                productPage.getContent(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService
                .getProducts(null, null, null, PageRequest.of(0, Integer.MAX_VALUE))
                .getContent();
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductRequest request) {
        return productService.createProduct(request);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id,
                                         @RequestBody ProductRequest request) {
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
    @PostMapping("/{id}/purchase")
    public String purchaseProduct(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int quantity
    ) {
        productService.purchaseProduct(id, quantity);
        return "Product purchased successfully";
    }
}
