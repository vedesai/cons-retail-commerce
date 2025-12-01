package com.deloitte.retail.mapper;

import com.deloitte.retail.dto.ProductRequest;
import com.deloitte.retail.dto.ProductResponse;
import com.deloitte.retail.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ProductMapper
 * 
 * @author Deloitte
 * @version 1.0.0
 */
class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = Mappers.getMapper(ProductMapper.class);
    }

    @Test
    void testToEntity() {
        ProductRequest request = ProductRequest.builder()
                .sku("SKU-001")
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .quantity(100)
                .category("Electronics")
                .brand("TestBrand")
                .isActive(true)
                .build();

        Product product = productMapper.toEntity(request);

        assertNotNull(product);
        assertEquals("SKU-001", product.getSku());
        assertEquals("Test Product", product.getName());
        assertEquals("Test Description", product.getDescription());
        assertEquals(new BigDecimal("99.99"), product.getPrice());
        assertEquals(100, product.getQuantity());
        assertEquals("Electronics", product.getCategory());
        assertEquals("TestBrand", product.getBrand());
        assertTrue(product.getIsActive());
        assertNull(product.getId()); // Should be ignored
    }

    @Test
    void testToResponse() {
        Product product = Product.builder()
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

        ProductResponse response = productMapper.toResponse(product);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("SKU-001", response.getSku());
        assertEquals("Test Product", response.getName());
        assertEquals("Test Description", response.getDescription());
        assertEquals(new BigDecimal("99.99"), response.getPrice());
        assertEquals(100, response.getQuantity());
        assertEquals("Electronics", response.getCategory());
        assertEquals("TestBrand", response.getBrand());
        assertTrue(response.getIsActive());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());
    }

    @Test
    void testUpdateEntityFromRequest() {
        Product product = Product.builder()
                .id(1L)
                .sku("SKU-001")
                .name("Original Name")
                .price(new BigDecimal("50.00"))
                .quantity(50)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ProductRequest request = ProductRequest.builder()
                .sku("SKU-002") // Should be ignored
                .name("Updated Name")
                .price(new BigDecimal("100.00"))
                .quantity(100)
                .build();

        productMapper.updateEntityFromRequest(request, product);

        assertEquals(1L, product.getId()); // Should not change
        assertEquals("SKU-001", product.getSku()); // Should not change
        assertEquals("Updated Name", product.getName());
        assertEquals(new BigDecimal("100.00"), product.getPrice());
        assertEquals(100, product.getQuantity());
    }
}
