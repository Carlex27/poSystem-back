package com.softeams.poSystem.core.mappers;

import com.softeams.poSystem.core.dtos.resumes.ResumeDashboardDto;
import com.softeams.poSystem.core.dtos.resumes.ResumeVentasDto;
import com.softeams.poSystem.core.dtos.resumes.SaleDashboard;
import com.softeams.poSystem.core.dtos.sales.SaleItemRequest;
import com.softeams.poSystem.core.dtos.sales.SaleItemResponse;
import com.softeams.poSystem.core.dtos.sales.SaleRequest;
import com.softeams.poSystem.core.dtos.sales.SaleResponse;
import com.softeams.poSystem.core.entities.Sale;
import com.softeams.poSystem.core.entities.SaleItem;
import com.softeams.poSystem.core.services.interfaces.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class SaleMapper implements ISaleMapper{
    private final IProductService productService;

    public Sale toEntity(SaleRequest saleRequest, Authentication authentication) {
        log.info("Mapping SaleRequest to Sale entity: {}", saleRequest);
        Long totalQuantity = saleRequest.items().stream()
                .mapToLong(SaleItemRequest::quantity)
                .sum();
        Sale sale = Sale.builder()
                .clientName(saleRequest.clientName())
                .vendedorName(authentication.getName())
                .saleDate(saleRequest.saleDate())
                .state(totalQuantity >= 20 ? "Mayoreo" : "Menudeo")
                .itemCount(totalQuantity)
                .build();
        Set<SaleItem> items = toSaleItems(saleRequest.items(), sale);
        sale.setItems(toSaleItems(saleRequest.items(),sale));
        return sale;
    }

    private Set<SaleItem> toSaleItems(List<SaleItemRequest> saleItemRequestList, Sale sale){
        log.info("Mapping SaleItemRequest list to SaleItem set: {}", saleItemRequestList);
        return saleItemRequestList.stream()
                .map(item -> {
                    var product = productService.getProductById(item.productId());
                    return SaleItem.builder()
                            .sale(sale)
                            .product(product)
                            .quantity(item.quantity())
                            .price(sale.getItemCount() >= 20 ? product.getPrecioMayoreo() : product.getPrecioNormal())
                            .build();
                })
                .collect(Collectors.toSet());
    }

    public SaleResponse toResponse(Sale sale) {
        log.info("Mapping Sale entity to SaleResponse: {}", sale.getId());
        return new SaleResponse(
                sale.getId(),
                sale.getClientName(),
                sale.getVendedorName(),
                sale.getSaleDate(),
                sale.getTotal(),
                sale.getState(),
                toSaleItemsResponse(sale.getItems()),
                sale.getItemCount()
        );
    }
    public List<SaleResponse> toResponse(List<Sale> sales) {
        log.info("Mapping Sale entity to SaleResponse: ");
        return sales.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Set<SaleItemResponse> toSaleItemsResponse(Set<SaleItem> saleItems) {
        log.info("Mapping SaleItem set to SaleItemResponse set:");
        return saleItems.stream()
                .map(item -> new SaleItemResponse(
                        item.getProduct().getSKU(),
                        item.getProduct().getNombre(),
                        item.getProduct().getMarca(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .collect(Collectors.toSet());
    }

    private SaleDashboard toSaleDashboard(Sale sale) {
        return new SaleDashboard(
                sale.getClientName(),
                sale.getSaleDate(),
                sale.getTotal(),
                sale.getItemCount()
        );
    }

    public ResumeVentasDto toResumeVentasDto(Long totalVentas, BigDecimal totalAmount) {
        log.info("Mapping Sale entity to ResumeVentasDto: ");
        return new ResumeVentasDto(
                totalVentas,
                totalAmount
        );
    }

    public ResumeDashboardDto toResumeDashboardDto(
            Long totalProductos,
            Long totalVentas,
            BigDecimal ingresosTotales,
            Long stockBajo,
            List<Sale> ventasRecientes
    ) {
        log.info("Mapping Sale entity to ResumeDashboardDto: ");
        return new ResumeDashboardDto(
                totalProductos,
                totalVentas == null ? 0 : totalVentas,
                ingresosTotales != null ? ingresosTotales : BigDecimal.ZERO,
                stockBajo,
                ventasRecientes != null
                        ? ventasRecientes.stream()
                        .map(this::toSaleDashboard)
                        .collect(Collectors.toList())
                        : Collections.emptyList()
        );

    }
}
