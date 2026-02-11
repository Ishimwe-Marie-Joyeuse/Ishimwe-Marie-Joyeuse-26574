package question4_E_Commerce_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import question4_E_Commerce_api.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private List<Product> products = new ArrayList<>();
    private Long nextId = 11L;

    public ProductController() {
        products.add(new Product(1L, "iPhone 15 Pro", "Latest Apple smartphone with A17 chip", 999.99, "Electronics", 50, "Apple"));
        products.add(new Product(2L, "Samsung Galaxy S24", "Flagship Android phone with advanced camera", 899.99, "Electronics", 45, "Samsung"));
        products.add(new Product(3L, "MacBook Air M3", "Lightweight laptop with powerful M3 processor", 1299.99, "Electronics", 30, "Apple"));
        products.add(new Product(4L, "Nike Air Max Shoes", "Comfortable running shoes with air cushioning", 129.99, "Clothing", 100, "Nike"));
        products.add(new Product(5L, "Adidas Track Jacket", "Sporty jacket for casual and athletic wear", 79.99, "Clothing", 75, "Adidas"));
        products.add(new Product(6L, "Levi's 501 Jeans", "Classic straight-fit denim jeans", 69.99, "Clothing", 0, "Levi's"));
        products.add(new Product(7L, "KitchenAid Stand Mixer", "Professional 5-quart mixer for baking", 379.99, "Home & Kitchen", 25, "KitchenAid"));
        products.add(new Product(8L, "Dyson V15 Vacuum", "Powerful cordless vacuum with laser detection", 649.99, "Home & Kitchen", 15, "Dyson"));
        products.add(new Product(9L, "Clean Code", "A handbook of agile software craftsmanship", 34.99, "Books", 200, "Prentice Hall"));
        products.add(new Product(10L, "Atomic Habits", "An easy and proven way to build good habits", 16.99, "Books", 150, "Penguin Random House"));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit) {
        
        List<Product> result = products;
        
        if (page != null && limit != null && page >= 0 && limit > 0) {
            int startIndex = page * limit;
            int endIndex = Math.min(startIndex + limit, products.size());
            
            if (startIndex < products.size()) {
                result = products.subList(startIndex, endIndex);
            } else {
                result = new ArrayList<>();
            }
        }
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Optional<Product> product = products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst();
        
        return product.isPresent() ? ResponseEntity.ok(product.get()) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> categoryProducts = products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(categoryProducts);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getProductsByBrand(@PathVariable String brand) {
        List<Product> brandProducts = products.stream()
                .filter(p -> p.getBrand().equalsIgnoreCase(brand))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(brandProducts);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> searchResults = products.stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                           p.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        
        List<Product> priceRangeProducts = products.stream()
                .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(priceRangeProducts);
    }

    @GetMapping("/in-stock")
    public ResponseEntity<List<Product>> getInStockProducts() {
        List<Product> inStockProducts = products.stream()
                .filter(p -> p.getStockQuantity() > 0)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(inStockProducts);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        product.setProductId(nextId++);
        products.add(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product updatedProduct) {
        
        Optional<Product> existingProduct = products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst();
        
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setCategory(updatedProduct.getCategory());
            product.setStockQuantity(updatedProduct.getStockQuantity());
            product.setBrand(updatedProduct.getBrand());
            
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Product> updateStock(
            @PathVariable Long productId,
            @RequestParam int quantity) {
        
        Optional<Product> existingProduct = products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst();
        
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setStockQuantity(quantity);
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        boolean removed = products.removeIf(p -> p.getProductId().equals(productId));
        
        return removed ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
