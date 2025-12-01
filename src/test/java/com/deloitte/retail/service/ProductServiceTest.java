package com.deloitte.retail.service;

import com.deloitte.retail.dto.ProductRequest;
import com.deloitte.retail.dto.ProductResponse;
import com.deloitte.retail.exception.DuplicateResourceException;
import com.deloitte.retail.exception.ResourceNotFoundException;
import com.deloitte.retail.mapper.ProductMapper;
import com.deloitte.retail.model.Product;
import com.deloitte.retail.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductService
 * 
 * @author Deloitte
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private ProductRequest productRequest;
    private Product product;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productRequest = ProductRequest.builder()
                .sku("SKU-001")
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .quantity(100)
                .category("Electronics")
                .brand("TestBrand")
                .isActive(true)
                .build();

        product = Product.builder()
                .id(1L)
                .sku("SKU-001")
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .quantity(100)
                .category("Electronics")
                .brand("TestBrand")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productResponse = ProductResponse.builder()
                .id(1L)
                .sku("SKU-001")
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .quantity(100)
                .category("Electronics")
                .brand("TestBrand")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateProduct_Success() {
        when(productRepository.existsBySku("SKU-001")).thenReturn(false);
        when(productMapper.toEntity(productRequest)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        ProductResponse result = productService.createProduct(productRequest);

        assertNotNull(result);
        assertEquals("SKU-001", result.getSku());
        assertEquals("Test Product", result.getName());
        verify(productRepository).existsBySku("SKU-001");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testCreateProduct_DuplicateSku() {
        when(productRepository.existsBySku("SKU-001")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> {
            productService.createProduct(productRequest);
        });

        verify(productRepository).existsBySku("SKU-001");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        ProductResponse result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productRepository).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(1L);
        });

        verify(productRepository).findById(1L);
    }

    @Test
    void testGetProductBySku_Success() {
        when(productRepository.findBySku("SKU-001")).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        ProductResponse result = productService.getProductBySku("SKU-001");

        assertNotNull(result);
        assertEquals("SKU-001", result.getSku());
        verify(productRepository).findBySku("SKU-001");
    }

    @Test
    void testGetProductBySku_NotFound() {
        when(productRepository.findBySku("SKU-001")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductBySku("SKU-001");
        });

        verify(productRepository).findBySku("SKU-001");
    }

    @Test
    void testGetAllProducts_Success() {
        Product product2 = Product.builder()
                .id(2L)
                .sku("SKU-002")
                .name("Product 2")
                .price(new BigDecimal("49.99"))
                .quantity(50)
                .build();

        ProductResponse response2 = ProductResponse.builder()
                .id(2L)
                .sku("SKU-002")
                .name("Product 2")
                .price(new BigDecimal("49.99"))
                .quantity(50)
                .build();

        when(productRepository.findAll()).thenReturn(Arrays.asList(product, product2));
        when(productMapper.toResponse(product)).thenReturn(productResponse);
        when(productMapper.toResponse(product2)).thenReturn(response2);

        List<ProductResponse> results = productService.getAllProducts();

        assertNotNull(results);
        assertEquals(2, results.size());
        verify(productRepository).findAll();
    }

    @Test
    void testGetActiveProducts_Success() {
        when(productRepository.findByIsActiveTrue()).thenReturn(Arrays.asList(product));
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        List<ProductResponse> results = productService.getActiveProducts();

        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(results.get(0).getIsActive());
        verify(productRepository).findByIsActiveTrue();
    }

    @Test
    void testUpdateProduct_Success() {
        ProductRequest updateRequest = ProductRequest.builder()
                .name("Updated Product")
                .price(new BigDecimal("149.99"))
                .quantity(200)
                .build();

        Product updatedProduct = Product.builder()
                .id(1L)
                .sku("SKU-001")
                .name("Updated Product")
                .price(new BigDecimal("149.99"))
                .quantity(200)
                .build();

        ProductResponse updatedResponse = ProductResponse.builder()
                .id(1L)
                .sku("SKU-001")
                .name("Updated Product")
                .price(new BigDecimal("149.99"))
                .quantity(200)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.toResponse(updatedProduct)).thenReturn(updatedResponse);

        ProductResponse result = productService.updateProduct(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        verify(productRepository).findById(1L);
        verify(productMapper).updateEntityFromRequest(eq(updateRequest), any(Product.class));
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_NotFound() {
        ProductRequest updateRequest = ProductRequest.builder()
                .name("Updated Product")
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(1L, updateRequest);
        });

        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository).existsById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(1L);
        });

        verify(productRepository).existsById(1L);
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    void testSearchProductsByName_Success() {
        when(productRepository.searchByName("Test")).thenReturn(Arrays.asList(product));
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        List<ProductResponse> results = productService.searchProductsByName("Test");

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(productRepository).searchByName("Test");
    }

    @Test
    void testGetProductsByCategory_Success() {
        when(productRepository.findByCategory("Electronics")).thenReturn(Arrays.asList(product));
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        List<ProductResponse> results = productService.getProductsByCategory("Electronics");

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(productRepository).findByCategory("Electronics");
    }
}
