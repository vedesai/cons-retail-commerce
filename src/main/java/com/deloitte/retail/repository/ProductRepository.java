package com.deloitte.retail.repository;

import com.deloitte.retail.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product entity
 * 
 * @author Deloitte
 * @version 1.0.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find product by SKU
     * 
     * @param sku Product SKU
     * @return Optional Product
     */
    Optional<Product> findBySku(String sku);

    /**
     * Check if product exists by SKU
     * 
     * @param sku Product SKU
     * @return true if exists, false otherwise
     */
    boolean existsBySku(String sku);

    /**
     * Find all active products
     * 
     * @return List of active products
     */
    List<Product> findByIsActiveTrue();

    /**
     * Find products by category
     * 
     * @param category Product category
     * @return List of products in the category
     */
    List<Product> findByCategory(String category);

    /**
     * Find products by brand
     * 
     * @param brand Product brand
     * @return List of products by brand
     */
    List<Product> findByBrand(String brand);

    /**
     * Search products by name containing the search term
     * 
     * @param name Search term
     * @return List of matching products
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> searchByName(@Param("name") String name);
}
