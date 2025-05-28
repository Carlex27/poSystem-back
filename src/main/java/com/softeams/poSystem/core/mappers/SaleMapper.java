package com.softeams.poSystem.core.mappers;

import com.softeams.poSystem.core.dtos.SaleItemRequest;
import com.softeams.poSystem.core.dtos.SaleItemResponse;
import com.softeams.poSystem.core.dtos.SaleRequest;
import com.softeams.poSystem.core.dtos.SaleResponse;
import com.softeams.poSystem.core.entities.Sale;
import com.softeams.poSystem.core.entities.SaleItem;
import com.softeams.poSystem.core.services.interfaces.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class SaleMapper {
    private final IProductService productService;

    public Sale toEntity(SaleRequest saleRequest, Authentication authentication) {
        log.info("Mapping SaleRequest to Sale entity: {}", saleRequest);
        Sale sale = Sale.builder()
                .clientName(saleRequest.clientName())
                .vendedorName(authentication.getName())
                .saleDate(saleRequest.saleDate())
                .state(saleRequest.state())
                .build();
        sale.setItems(toSaleItems(saleRequest.items(),sale));
        return sale;
    }

    private Set<SaleItem> toSaleItems(List<SaleItemRequest> saleItemRequestList, Sale sale){
        log.info("Mapping SaleItemRequest list to SaleItem set: {}", saleItemRequestList);
        return saleItemRequestList.stream()
                .map(item -> SaleItem.builder()
                        .sale(sale)
                        .product(productService.getProductById(item.productId()))
                        .quantity(item.quantity())
                        .price(item.price())
                        .build())
                .collect(Collectors.toSet());
    }

    public SaleResponse toResponse(Sale sale) {
        log.info("Mapping Sale entity to SaleResponse: {}", sale);
        return new SaleResponse(
                sale.getId(),
                sale.getClientName(),
                sale.getVendedorName(),
                sale.getSaleDate(),
                sale.getTotal(),
                sale.getState()
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
}
