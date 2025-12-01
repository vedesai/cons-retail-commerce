package com.deloitte.retail.controller;

import com.deloitte.retail.dto.ProductRequest;
import com.deloitte.retail.dto.ProductResponse;
import com.deloitte.retail.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Product operations
 * 
 * @author Deloitte
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    /**
     * Create a new product
     * 
     * @param request ProductRequest DTO
     * @return ResponseEntity with ProductResponse
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        log.info("POST /api/v1/products - Creating product");
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get product by ID
     * 
     * @param id Product ID
     * @return ResponseEntity with ProductResponse
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        log.info("GET /api/v1/products/{} - Fetching product", id);
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get product by SKU
     * 
     * @param sku Product SKU
     * @return ResponseEntity with ProductResponse
     */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponse> getProductBySku(@PathVariable String sku) {
        log.info("GET /api/v1/products/sku/{} - Fetching product by SKU", sku);
        ProductResponse response = productService.getProductBySku(sku);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all products
     * 
     * @return ResponseEntity with List of ProductResponse
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("GET /api/v1/products - Fetching all products");
        List<ProductResponse> responses = productService.getAllProducts();
        return ResponseEntity.ok(responses);
    }

    /**
     * Get all active products
     * 
     * @return ResponseEntity with List of ProductResponse
     */
    @GetMapping("/active")
    public ResponseEntity<List<ProductResponse>> getActiveProducts() {
        log.info("GET /api/v1/products/active - Fetching active products");
        List<ProductResponse> responses = productService.getActiveProducts();
        return ResponseEntity.ok(responses);
    }

    /**
     * Search products by name
     * 
     * @param name Search term
     * @return ResponseEntity with List of ProductResponse
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String name) {
        log.info("GET /api/v1/products/search?name={} - Searching products", name);
        List<ProductResponse> responses = productService.searchProductsByName(name);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get products by category
     * 
     * @param category Product category
     * @return ResponseEntity with List of ProductResponse
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
        log.info("GET /api/v1/products/category/{} - Fetching products by category", category);
        List<ProductResponse> responses = productService.getProductsByCategory(category);
        return ResponseEntity.ok(responses);
    }

    /**
     * Update product by ID
     * 
     * @param id Product ID
     * @param request ProductRequest DTO
     * @return ResponseEntity with ProductResponse
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        log.info("PUT /api/v1/products/{} - Updating product", id);
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete product by ID
     * 
     * @param id Product ID
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("DELETE /api/v1/products/{} - Deleting product", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
