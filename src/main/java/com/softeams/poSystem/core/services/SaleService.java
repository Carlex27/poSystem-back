package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.dtos.SaleResponse;
import com.softeams.poSystem.core.entities.Sale;
import com.softeams.poSystem.core.repositories.SaleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaleService {
    private final SaleRepository saleRepository;
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
    public List<Sale> getSalesBySaleDate(String saleDate) {
        log.info("Fetching sales by sale date: {}", saleDate);
        return saleRepository.findBySaleDate(saleDate);
    }
    //UPDATE AND DELETE

    //Por logica de negocio, no se permite actualizar una venta una vez creada.

    //LOGIC

}
