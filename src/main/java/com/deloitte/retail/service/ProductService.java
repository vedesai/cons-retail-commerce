package com.deloitte.retail.service;

import com.deloitte.retail.dto.ProductRequest;
import com.deloitte.retail.dto.ProductResponse;
import com.deloitte.retail.exception.DuplicateResourceException;
import com.deloitte.retail.exception.ResourceNotFoundException;
import com.deloitte.retail.mapper.ProductMapper;
import com.deloitte.retail.model.Product;
import com.deloitte.retail.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Product operations
 * 
 * @author Deloitte
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Create a new product
     * 
     * @param request ProductRequest DTO
     * @return ProductResponse DTO
     * @throws DuplicateResourceException if product with same SKU already exists
     */
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating product with SKU: {}", request.getSku());
        
        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("Product with SKU " + request.getSku() + " already exists");
        }

        Product product = productMapper.toEntity(request);
        if (request.getIsActive() == null) {
            product.setIsActive(true);
        }
        
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {}", savedProduct.getId());
        
        return productMapper.toResponse(savedProduct);
    }

    /**
     * Get product by ID
     * 
     * @param id Product ID
     * @return ProductResponse DTO
     * @throws ResourceNotFoundException if product not found
     */
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        
        return productMapper.toResponse(product);
    }

    /**
     * Get product by SKU
     * 
     * @param sku Product SKU
     * @return ProductResponse DTO
     * @throws ResourceNotFoundException if product not found
     */
    @Transactional(readOnly = true)
    public ProductResponse getProductBySku(String sku) {
        log.info("Fetching product with SKU: {}", sku);
        
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
        
        return productMapper.toResponse(product);
    }

    /**
     * Get all products
     * 
     * @return List of ProductResponse DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products");
        
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all active products
     * 
     * @return List of active ProductResponse DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getActiveProducts() {
        log.info("Fetching all active products");
        
        return productRepository.findByIsActiveTrue().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update product by ID
     * 
     * @param id Product ID
     * @param request ProductRequest DTO
     * @return ProductResponse DTO
     * @throws ResourceNotFoundException if product not found
     */
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product with ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        productMapper.updateEntityFromRequest(request, product);
        Product updatedProduct = productRepository.save(product);
        
        log.info("Product updated successfully with ID: {}", updatedProduct.getId());
        
        return productMapper.toResponse(updatedProduct);
    }

    /**
     * Delete product by ID
     * 
     * @param id Product ID
     * @throws ResourceNotFoundException if product not found
     */
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        
        productRepository.deleteById(id);
        log.info("Product deleted successfully with ID: {}", id);
    }

    /**
     * Search products by name
     * 
     * @param name Search term
     * @return List of matching ProductResponse DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProductsByName(String name) {
        log.info("Searching products by name: {}", name);
        
        return productRepository.searchByName(name).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get products by category
     * 
     * @param category Product category
     * @return List of ProductResponse DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(String category) {
        log.info("Fetching products by category: {}", category);
        
        return productRepository.findByCategory(category).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }
}
