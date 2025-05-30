package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.dtos.SaleItemResponse;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaleService {
    private final SaleRepository saleRepository;
    private final IProductService productService;
    //CRUD

    //CREATE

    public Sale createSale(Sale sale) {
        log.info("Creating sale: {}", sale.getClientName());
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

    public Set<SaleItem> getSaleItemsById(Long id) {
        log.info("GAAAAAAAA");
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found with id: " + id));
        return sale.getItems();
    }

    public List<Sale> getSalesByDate(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching sales by date: {}", startDate);
        return saleRepository.findBySaleDateBetween(startDate, endDate);
    }

    public BigDecimal getTotalVentas(LocalDateTime start, LocalDateTime end) {
        log.info("Calculating total sales from {} to {}", start, end);
        return saleRepository.getTotalVentas(start, end);
    }

    public long countSalesInRange(LocalDateTime start, LocalDateTime end) {
        log.info("Counting sales in range from {} to {}", start, end);
        return saleRepository.countSalesInRange(start, end);
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

        Integer itemCount = sale.getItems().stream()
                .mapToInt(SaleItem::getQuantity)
                .sum();

        //Crear la venta en la base de datos
        Sale createdSale = createSale(sale);
        log.info("Sale created successfully with id: {}", createdSale.getId());
        return new SaleResponse(
                createdSale.getId(),
                createdSale.getClientName(),
                createdSale.getVendedorName(),
                createdSale.getSaleDate(),
                createdSale.getTotal(),
                createdSale.getState(),
                createdSale.getItems().stream()
                        .map(item -> new SaleItemResponse(
                                item.getProduct().getSKU(),
                                item.getProduct().getNombre(),
                                item.getProduct().getMarca(),
                                item.getQuantity(),
                                item.getPrice()
                        )).collect(Collectors.toSet()),
                itemCount
        );
    }
}
