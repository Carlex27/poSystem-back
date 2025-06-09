package com.softeams.poSystem.core.mappers;

import com.softeams.poSystem.core.dtos.product.ProductRequest;
import com.softeams.poSystem.core.dtos.product.ProductResponse;
import com.softeams.poSystem.core.entities.Product;
import com.softeams.poSystem.core.mappers.interfaces.IProductMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper implements IProductMapper {
    public ProductResponse toDto(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductResponse(
                product.getSKU(),
                product.getNombre(),
                product.getMarca(),
                product.getGradosAlcohol(),
                product.getTamanio(),
                product.getPrecioNormal(),
                product.getPrecioMayoreo(),
                product.getStock()
        );
    }
    public Product toEntity(ProductRequest product) {
        if (product == null) {
            return null;
        }
        return Product.builder()
                .SKU(product.getSku())
                .nombre(product.getNombre())
                .marca(product.getMarca())
                .gradosAlcohol(product.getGradosAlcohol())
                .tamanio(product.getTamanio())
                .precioNormal(product.getPrecioNormal())
                .precioMayoreo(product.getPrecioMayoreo())
                .stock(product.getStock())
                .isActive(true)
                .build();
    }
    public List<Product> toEntity(List<ProductRequest> product) {
        if (product == null) {
            return null;
        }
        return product.stream()
                .map(this::toEntity)
                .toList();
    }

}
