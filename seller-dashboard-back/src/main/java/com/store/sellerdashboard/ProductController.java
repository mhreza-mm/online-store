package com.store.sellerdashboard;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/seller/products")
@CrossOrigin(origins={"http://localhost:3001","http://localhost:3000"})
@RequiredArgsConstructor
public class ProductController {
  private final ProductRepository repository;

  @GetMapping public List<Product> list(@RequestParam(required=false) String search) {
    var all = repository.findAll();
    if (search == null || search.isBlank()) return all;
    var q = search.toLowerCase();
    return all.stream().filter(p -> p.getTitle().toLowerCase().contains(q) || (p.getBrand()!=null && p.getBrand().toLowerCase().contains(q))).toList();
  }
  @GetMapping("/stats") public Map<String,Object> stats() {
    var all=repository.findAll();
    return Map.of("totalProducts", all.size(), "activeProducts", all.stream().filter(p->p.getStock()>0).count(), "lowStock", all.stream().filter(p->p.getStock()>0 && p.getStock()<10).count(), "outOfStock", all.stream().filter(p->p.getStock()==0).count());
  }
  @PostMapping @ResponseStatus(HttpStatus.CREATED) public Product create(@RequestBody ProductRequest request) { return repository.save(request.toProduct(new Product())); }
  @PutMapping("/{id}") public Product update(@PathVariable Long id, @RequestBody ProductRequest request) { var p=repository.findById(id).orElseThrow(); return repository.save(request.toProduct(p)); }
  @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { repository.deleteById(id); }

  @Data public static class ProductRequest {
    private String title; private Double price; private String image; private String type; private String brand; private Integer stock;
    Product toProduct(Product p) { p.setTitle(title); p.setPrice(price==null?0:price); p.setImage(image); p.setType(type); p.setBrand(brand); p.setStock(stock==null?0:Math.max(0,stock)); return p; }
  }
}
