package com.softeams.poSystem.core.mappers;

import com.softeams.poSystem.core.dtos.product.ProductRequest;
import com.softeams.poSystem.core.dtos.product.ProductResponse;
import com.softeams.poSystem.core.entities.Department;
import com.softeams.poSystem.core.entities.Product;
import com.softeams.poSystem.core.mappers.interfaces.IProductMapper;
import com.softeams.poSystem.core.services.interfaces.IDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper implements IProductMapper {
    private final IDepartmentService departmentService;
    public ProductResponse toDto(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductResponse(
                product.getSKU(),
                product.getNombre(),
                product.getDepartment().getName(),
                product.getPrecioCosto(),
                product.getPrecioVenta(),
                product.getPrecioMayoreo(),
                product.getStock(),
                product.getPrecioPiezaVenta(),
                product.getPrecioPiezaMayoreo(),
                product.getStockMinimo(),
                product.getMinimoMayoreo()
        );
    }
    public Product toEntity(ProductRequest product) {
        if (product == null) {
            return null;
        }
        Department department = departmentService.getDepartmentByName(product.getDepartamento());
        return Product.builder()
                .SKU(product.getSku())
                .nombre(product.getNombre())
                .precioCosto(product.getPrecioCosto())
                .precioVenta(product.getPrecioVenta())
                .precioMayoreo(product.getPrecioMayoreo())
                .stock(product.getStock())
                .stockMinimo(product.getStockMinimo())
                .minimoMayoreo(product.getMinimoMayoreo())
                .department(department)
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
