package com.store.onlinestore.repository;

import com.store.onlinestore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByType(String type, Pageable pageable);

    Page<Product> findByBrand(String brand, Pageable pageable);

    Page<Product> findByBrandAndType(String brand, String type, Pageable pageable);

    // برای سرچ صرفاً بر اساس عنوان
    Page<Product> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // برای سرچ همراه با فیلتر نوع
    Page<Product> findByTypeAndTitleContainingIgnoreCase(String type, String title, Pageable pageable);

    // برای سرچ همراه با فیلتر برند
    Page<Product> findByBrandAndTitleContainingIgnoreCase(String brand, String title, Pageable pageable);

    // برای سرچ همراه با هر دو فیلتر
    Page<Product> findByBrandAndTypeAndTitleContainingIgnoreCase(String brand, String type, String title, Pageable pageable);
}
