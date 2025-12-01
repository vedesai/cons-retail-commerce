package com.deloitte.retail.controller;

import com.deloitte.retail.dto.ProductRequest;
import com.deloitte.retail.dto.ProductResponse;
import com.deloitte.retail.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ProductController
 * 
 * @author Deloitte
 * @version 1.0.0
 */
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequest productRequest;
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
    @WithMockUser
    void testCreateProduct_Success() throws Exception {
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(productResponse);

        mockMvc.perform(post("/api/v1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.sku").value("SKU-001"))
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService).createProduct(any(ProductRequest.class));
    }

    @Test
    @WithMockUser
    void testCreateProduct_ValidationError() throws Exception {
        ProductRequest invalidRequest = ProductRequest.builder()
                .sku("") // Invalid: empty SKU
                .name("") // Invalid: empty name
                .build();

        mockMvc.perform(post("/api/v1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(productService, never()).createProduct(any(ProductRequest.class));
    }

    @Test
    @WithMockUser
    void testGetProductById_Success() throws Exception {
        when(productService.getProductById(1L)).thenReturn(productResponse);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.sku").value("SKU-001"));

        verify(productService).getProductById(1L);
    }

    @Test
    @WithMockUser
    void testGetProductBySku_Success() throws Exception {
        when(productService.getProductBySku("SKU-001")).thenReturn(productResponse);

        mockMvc.perform(get("/api/v1/products/sku/SKU-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU-001"));

        verify(productService).getProductBySku("SKU-001");
    }

    @Test
    @WithMockUser
    void testGetAllProducts_Success() throws Exception {
        List<ProductResponse> products = Arrays.asList(productResponse);
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(productService).getAllProducts();
    }

    @Test
    @WithMockUser
    void testGetActiveProducts_Success() throws Exception {
        List<ProductResponse> products = Arrays.asList(productResponse);
        when(productService.getActiveProducts()).thenReturn(products);

        mockMvc.perform(get("/api/v1/products/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(productService).getActiveProducts();
    }

    @Test
    @WithMockUser
    void testSearchProducts_Success() throws Exception {
        List<ProductResponse> products = Arrays.asList(productResponse);
        when(productService.searchProductsByName("Test")).thenReturn(products);

        mockMvc.perform(get("/api/v1/products/search")
                        .param("name", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(productService).searchProductsByName("Test");
    }

    @Test
    @WithMockUser
    void testGetProductsByCategory_Success() throws Exception {
        List<ProductResponse> products = Arrays.asList(productResponse);
        when(productService.getProductsByCategory("Electronics")).thenReturn(products);

        mockMvc.perform(get("/api/v1/products/category/Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(productService).getProductsByCategory("Electronics");
    }

    @Test
    @WithMockUser
    void testUpdateProduct_Success() throws Exception {
        when(productService.updateProduct(eq(1L), any(ProductRequest.class))).thenReturn(productResponse);

        mockMvc.perform(put("/api/v1/products/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(productService).updateProduct(eq(1L), any(ProductRequest.class));
    }

    @Test
    @WithMockUser
    void testDeleteProduct_Success() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/products/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }
}
