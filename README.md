# 🛍️ Product Catalogue Service

> Product catalog, categories, reviews, and PC builder compatibility for the CS02 E-Commerce Platform

## 📋 Overview

The Product Catalogue Service is the core service for managing all product-related data including computer components, categories, customer reviews, and PC builder compatibility checking. It provides comprehensive search, filtering, and stock management capabilities.

## 🛠️ Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Language | Java | 17 |
| Framework | Spring Boot | 4.0.0 |
| Database | MongoDB | Latest |
| Build Tool | Maven | 3.x |
| ORM | Spring Data MongoDB | Latest |

## 🚀 API Endpoints

### Products

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/api/products` | No | Get products with filtering/pagination |
| `GET` | `/api/products/search` | No | Search products by keyword |
| `GET` | `/api/products/{id}` | No | Get product by ID |
| `GET` | `/api/products/{id}/related` | No | Get related products |
| `GET` | `/api/products/featured` | No | Get featured products |
| `GET` | `/api/products/low-stock` | Admin | Get low stock products |
| `GET` | `/api/products/deals` | No | Get products on sale |
| `POST` | `/api/products` | Admin | Create product |
| `PUT` | `/api/products/{id}` | Admin | Update product |
| `DELETE` | `/api/products/{id}` | Admin | Delete product |
| `POST` | `/api/products/validate-stock` | Internal | Validate stock availability |
| `POST` | `/api/products/reduce-stock` | Internal | Reduce stock after order |

### Categories

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/api/products/categories` | No | Get all categories |
| `GET` | `/api/products/categories/{slug}` | No | Get category by slug |
| `POST` | `/api/products/categories` | Admin | Create category |
| `PUT` | `/api/products/categories/{id}` | Admin | Update category |
| `DELETE` | `/api/products/categories/{id}` | Admin | Delete category |

### Reviews

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/api/products/{id}/reviews` | No | Get product reviews |
| `POST` | `/api/products/{id}/reviews` | Yes | Add product review |
| `PUT` | `/api/products/reviews/{id}` | Admin | Update review |
| `DELETE` | `/api/products/reviews/{id}` | Admin | Delete review |

### PC Builder

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/api/builder/compatibility` | No | Check component compatibility |
| `GET` | `/api/builder/compatible/{type}` | No | Get compatible components |
| `GET` | `/api/builder/suggestions` | No | Get component suggestions |

### Query Parameters for `/api/products`

| Parameter | Type | Description |
|-----------|------|-------------|
| `category` | string | Filter by category |
| `subcategory` | string | Filter by subcategory |
| `brand` | string | Filter by brand |
| `minPrice` | number | Minimum price |
| `maxPrice` | number | Maximum price |
| `inStock` | boolean | Only in-stock items |
| `sort` | string | Sort field (price, rating, name) |
| `order` | string | Sort order (asc, desc) |
| `page` | number | Page number (0-indexed) |
| `size` | number | Page size (default 20) |

## 📊 Data Models

### Product

```java
{
  "id": "string",
  "name": "NVIDIA GeForce RTX 4090",
  "sku": "GPU-RTX4090-001",
  "price": 1599.99,
  "salePrice": 1499.99,
  "category": "components",
  "subcategory": "graphics-cards",
  "brand": "NVIDIA",
  "description": "string",
  "shortDescription": "string",
  "specs": {
    "memory": "24GB GDDR6X",
    "coreClock": "2520 MHz",
    "boostClock": "2610 MHz",
    "tdp": "450W"
  },
  "compatibility": {
    "socket": null,
    "formFactor": "ATX",
    "psuRecommended": 850,
    "pciSlots": 3
  },
  "imageUrl": "string",
  "images": ["string"],
  "stockLevel": 25,
  "lowStockThreshold": 5,
  "rating": 4.8,
  "reviewCount": 156,
  "tags": ["gaming", "high-end", "ray-tracing"],
  "isActive": true,
  "isFeatured": true,
  "onSale": true,
  "allowPreorder": false,
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### Category

```java
{
  "id": "string",
  "name": "Graphics Cards",
  "slug": "graphics-cards",
  "description": "string",
  "imageUrl": "string",
  "parentId": "components-id",
  "order": 1,
  "isActive": true
}
```

### Review

```java
{
  "id": "string",
  "productId": "string",
  "userId": "uuid",
  "userName": "string",
  "rating": 5,
  "title": "Amazing GPU!",
  "comment": "string",
  "isVerifiedPurchase": true,
  "isApproved": true,
  "helpfulCount": 42,
  "createdAt": "datetime"
}
```

## 🔧 Configuration

### Application Properties

```yaml
server:
  port: 8082

spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/CSO2_product_catalogue_service
      database: CSO2_product_catalogue_service

logging:
  level:
    com.cs02.catalog: DEBUG
```

### Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `SPRING_DATA_MONGODB_URI` | No | `mongodb://localhost:27017` | MongoDB URI |
| `SPRING_DATA_MONGODB_DATABASE` | No | `CSO2_product_catalogue_service` | Database name |
| `SERVER_PORT` | No | `8082` | Service port |

## 📦 Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
</dependencies>
```

## 🏃 Running the Service

### Local Development

```bash
cd backend/product-catalogue-service

# Using Maven Wrapper
./mvnw spring-boot:run

# Or with Maven
mvn spring-boot:run
```

### Docker

```bash
cd backend/product-catalogue-service

# Build JAR
./mvnw clean package -DskipTests

# Build Docker image
docker build -t cs02/product-catalogue-service .

# Run container
docker run -p 8082:8082 \
  -e SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/CSO2_product_catalogue_service \
  cs02/product-catalogue-service
```

## 🗄️ Database Requirements

- **MongoDB** running on port `27017`
- Database: `CSO2_product_catalogue_service`
- Collections: `products`, `categories`, `reviews`

### Sample Product Categories

| Category | Subcategories |
|----------|---------------|
| components | graphics-cards, processors, motherboards, memory, storage, power-supplies, cases, cooling |
| peripherals | keyboards, mice, headsets, monitors, webcams |
| pre-builts | gaming-pcs, workstations, laptops |
| accessories | cables, adapters, tools, cleaning |

## ✅ Features - Completion Status

| Feature | Status | Notes |
|---------|--------|-------|
| Product CRUD | ✅ Complete | Full create, read, update, delete |
| Product search | ✅ Complete | Keyword-based search |
| Product filtering | ✅ Complete | Category, price, brand, stock |
| Pagination | ✅ Complete | Page-based results |
| Sorting | ✅ Complete | Multiple sort options |
| Categories CRUD | ✅ Complete | Hierarchical categories |
| Reviews CRUD | ✅ Complete | Customer reviews |
| Rating calculation | ✅ Complete | Auto-calculated averages |
| Stock management | ✅ Complete | Track and update stock |
| Low stock alerts | ✅ Complete | Threshold-based alerts |
| Featured products | ✅ Complete | Homepage display |
| Deals/Sale products | ✅ Complete | Sale price support |
| PC Builder compatibility | ✅ Complete | Component matching |
| Component suggestions | ✅ Complete | Smart recommendations |
| Stock validation | ✅ Complete | Pre-checkout check |
| Stock reduction | ✅ Complete | Post-order deduction |

### **Overall Completion: 95%** ✅

## ❌ Not Implemented / Future Enhancements

| Feature | Priority | Notes |
|---------|----------|-------|
| Full-text search (Elasticsearch) | Medium | Currently basic search |
| Product image upload | Medium | Currently URL references |
| Product variants (colors, sizes) | Low | Single variant per product |
| Price history tracking | Low | Historical pricing |
| Bulk import/export | Medium | CSV/Excel operations |
| Product comparisons | Low | Side-by-side compare |
| Inventory forecasting | Low | Predictive stock |

## 📁 Project Structure

```
product-catalogue-service/
├── src/
│   ├── main/
│   │   ├── java/com/cs02/catalog/
│   │   │   ├── ProductCatalogueApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── ProductController.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── ReviewController.java
│   │   │   │   └── BuilderController.java
│   │   │   ├── model/
│   │   │   │   ├── Product.java
│   │   │   │   ├── Category.java
│   │   │   │   └── Review.java
│   │   │   ├── repository/
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── CategoryRepository.java
│   │   │   │   └── ReviewRepository.java
│   │   │   └── service/
│   │   │       ├── ProductService.java
│   │   │       ├── CategoryService.java
│   │   │       ├── ReviewService.java
│   │   │       └── CompatibilityService.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── pom.xml
├── Dockerfile
└── README.md
```

## 🧪 Testing

```bash
# Run unit tests
./mvnw test

# Test endpoints
curl http://localhost:8082/api/products
curl http://localhost:8082/api/products?category=components&subcategory=graphics-cards
curl http://localhost:8082/api/products/featured
curl http://localhost:8082/api/products/categories
curl http://localhost:8082/api/products/search?q=RTX
```

## 🔗 Related Services

- [API Gateway](../api-gateway/README.md) - Routes `/api/products/*` and `/api/builder/*`
- [Order Service](../order-service/README.md) - Validates and reduces stock
- [Shopping Cart Service](../shoppingcart-wishlist-service/README.md) - Product data enrichment
- [AI Service](../AI-service/README.md) - Product recommendations

## 📝 Notes

- Service runs on port **8082**
- Uses **MongoDB** for flexible product schemas
- Product specs and compatibility are stored as embedded documents
- Reviews automatically update product rating averages
- Stock levels are validated before checkout
- Low stock threshold triggers alerts at configured level
