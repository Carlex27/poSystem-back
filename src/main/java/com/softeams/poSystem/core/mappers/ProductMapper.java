package com.softeams.poSystem.core.mappers;

import com.softeams.poSystem.core.dtos.product.ProductRequest;
import com.softeams.poSystem.core.dtos.product.ProductResponse;
import com.softeams.poSystem.core.entities.Department;
import com.softeams.poSystem.core.entities.Product;
import com.softeams.poSystem.core.mappers.interfaces.IProductMapper;
import com.softeams.poSystem.core.services.interfaces.IDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
                product.getId(),
                product.getSKU(),
                product.getNombre(),
                product.getDepartment().getName(),
                product.getPrecioCosto(),
                product.getPrecioVenta(),
                product.getPrecioMayoreo(),
                product.getPrecioPiezaVenta(),
                product.getPrecioPiezaMayoreo(),
                product.getUnidadesPorPresentacion(),
                product.getStock(),
                product.getStockPorUnidades(),
                product.getStockMinimo(),
                product.getMinimoMayoreo(),
                product.getImagePath()
        );
    }

    public List<ProductResponse> toDto(List<Product> products) {
        if (products == null) {
            return null;
        }
        return products.stream()
                .map(this::toDto)
                .toList();
    }

    public Product toEntity(ProductRequest product) {
        if (product == null) {
            return null;
        }
        Department department = departmentService.getDepartmentByName(product.getDepartamento());
        BigDecimal precioPiezaMayoreo = product.getPrecioMayoreo()
                .divide(
                        new BigDecimal(product.getUnidadesPorPresentacion()),
                        2, // scale: number of decimal places
                        BigDecimal.ROUND_HALF_UP // rounding mode
                );
        BigDecimal stockPorUnidades = product.getStock()
            .multiply(BigDecimal.valueOf(product.getUnidadesPorPresentacion()));

        return Product.builder()
                .SKU(product.getSku())
                .nombre(product.getNombre())
                .precioCosto(product.getPrecioCosto())
                .precioVenta(product.getPrecioVenta())
                .precioMayoreo(product.getPrecioMayoreo())
                .precioPiezaVenta(product.getPrecioUnidadVenta())
                .precioPiezaMayoreo(precioPiezaMayoreo)
                .stock(product.getStock())
                .stockMinimo(product.getStockMinimo())
                .stockPorUnidades(stockPorUnidades.intValue())
                .minimoMayoreo(product.getMinimoMayoreo())
                .unidadesPorPresentacion(product.getUnidadesPorPresentacion())
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
