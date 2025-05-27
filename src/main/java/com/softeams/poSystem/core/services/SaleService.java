package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.dtos.SaleResponse;
import com.softeams.poSystem.core.entities.Sale;
import com.softeams.poSystem.core.entities.SaleItem;
import com.softeams.poSystem.core.repositories.SaleRepository;
import com.softeams.poSystem.core.services.interfaces.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaleService {
    private final SaleRepository saleRepository;
    private final IProductService productService;
    //CRUD

    //CREATE
    public Sale createSale(Sale sale) {
        log.info("Creating sale: {}", sale);
        return saleRepository.save(sale);
    }
    //READ

    public List<Sale> getAllSales() {
        log.info("Fetching all sales");
        return saleRepository.findAll();
    }
    public Sale getSaleById(Long id) {
        log.info("Fetching sale by id: {}", id);
        return saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found with id: " + id));
    }

    //UPDATE AND DELETE

    //Por logica de negocio, no se permite actualizar una venta una vez creada.

    //LOGIC
    @Transactional
    public SaleResponse creatingSale(Sale sale) {
        log.info("Creating sale with details:");
        Set<SaleItem> saleItems = sale.getItems();
        //Calcular el total de la venta
        BigDecimal totalAmount = saleItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("Total amount for sale: {}", totalAmount);
        sale.setTotal(totalAmount);

        //Actualizar los productos en el inventario

        productService.updateStockAfterSale(sale.getItems());

        //Crear la venta en la base de datos
        Sale createdSale = createSale(sale);
        log.info("Sale created successfully with id: {}", createdSale.getId());
        return new SaleResponse(
                createdSale.getId(),
                createdSale.getClientName(),
                createdSale.getVendedorName(),
                createdSale.getSaleDate(),
                createdSale.getTotal(),
                createdSale.getState()
        );
    }
}
