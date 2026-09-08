# Seller Dashboard Backend

Standalone Spring Boot API for the seller dashboard. It uses the same `products` table as the buyer application and runs on port `8081`.

```powershell
./mvnw spring-boot:run
```

Endpoints: `GET /api/seller/products`, `GET /api/seller/products/stats`, `POST /api/seller/products`, `PUT /api/seller/products/{id}`, `DELETE /api/seller/products/{id}`.
