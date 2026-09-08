package com.store.onlinestore.service;

import com.store.onlinestore.dto.ProductRequest;
import com.store.onlinestore.dto.ProductResponse;
import com.store.onlinestore.entity.Product;
import com.store.onlinestore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductResponse> getProducts(String type,
                                             String brand,
                                             String search,
                                             Pageable pageable) {

        Page<Product> productPage;

        boolean hasSearch = (search != null && !search.trim().isEmpty());
        boolean hasType = (type != null && !type.trim().isEmpty());
        boolean hasBrand = (brand != null && !brand.trim().isEmpty());

        if (hasBrand && hasType && hasSearch) {
            productPage = productRepository
                    .findByBrandAndTypeAndTitleContainingIgnoreCase(brand, type, search, pageable);
        } else if (hasBrand && hasType) {
            productPage = productRepository.findByBrandAndType(brand, type, pageable);
        } else if (hasBrand && hasSearch) {
            productPage = productRepository.findByBrandAndTitleContainingIgnoreCase(brand, search, pageable);
        } else if (hasType && hasSearch) {
            productPage = productRepository.findByTypeAndTitleContainingIgnoreCase(type, search, pageable);
        } else if (hasBrand) {
            productPage = productRepository.findByBrand(brand, pageable);
        } else if (hasType) {
            productPage = productRepository.findByType(type, pageable);
        } else if (hasSearch) {
            productPage = productRepository.findByTitleContainingIgnoreCase(search, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        return productPage.map(this::mapToResponse);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return mapToResponse(product);
    }

    public ProductResponse createProduct(ProductRequest request) {

        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        product.setImage(request.getImage());
        product.setType(request.getType());
        product.setBrand(request.getBrand());

        // مقدار پیشفرض موجودی
        product.setStock(request.getStock() != null ? request.getStock() : 0);

        return mapToResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setTitle(request.getTitle());
        existing.setPrice(request.getPrice());
        existing.setImage(request.getImage());
        existing.setType(request.getType());
        existing.setBrand(request.getBrand());

        if (request.getStock() != null) {
            existing.setStock(request.getStock());
        }

        return mapToResponse(productRepository.save(existing));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }


    @Transactional
    public void purchaseProduct(Long productId, int quantity) {

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        product.setStock(product.getStock() - quantity);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .image(product.getImage())
                .type(product.getType())
                .brand(product.getBrand())
                .stock(product.getStock())
                .build();
    }
}
