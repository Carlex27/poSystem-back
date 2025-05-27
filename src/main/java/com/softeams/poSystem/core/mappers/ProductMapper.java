package com.softeams.poSystem.core.mappers;

import com.softeams.poSystem.core.dtos.ProductRequest;
import com.softeams.poSystem.core.dtos.ProductResponse;
import com.softeams.poSystem.core.entities.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper implements IProductMapper{
    public ProductResponse toDto(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductResponse(
                product.getId(),
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
                .nombre(product.nombre())
                .marca(product.marca())
                .gradosAlcohol(product.gradosAlcohol())
                .tamanio(product.tamanio())
                .precioNormal(product.precioNormal())
                .precioMayoreo(product.precioMayoreo())
                .stock(product.stock())
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
