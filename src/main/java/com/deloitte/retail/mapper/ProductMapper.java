package com.deloitte.retail.mapper;

import com.deloitte.retail.dto.ProductRequest;
import com.deloitte.retail.dto.ProductResponse;
import com.deloitte.retail.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper interface for Product entity and DTOs
 * 
 * @author Deloitte
 * @version 1.0.0
 */
@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    /**
     * Map ProductRequest to Product entity
     * 
     * @param request ProductRequest DTO
     * @return Product entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductRequest request);

    /**
     * Map Product entity to ProductResponse DTO
     * 
     * @param product Product entity
     * @return ProductResponse DTO
     */
    ProductResponse toResponse(Product product);

    /**
     * Update Product entity from ProductRequest
     * 
     * @param request ProductRequest DTO
     * @param product Product entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sku", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(ProductRequest request, @MappingTarget Product product);
}
