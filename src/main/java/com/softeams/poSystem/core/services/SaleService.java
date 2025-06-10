package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.dtos.sales.SaleItemResponse;
import com.softeams.poSystem.core.dtos.sales.SaleResponse;
import com.softeams.poSystem.core.entities.InventoryEntry;
import com.softeams.poSystem.core.entities.Product;
import com.softeams.poSystem.core.entities.Sale;
import com.softeams.poSystem.core.entities.SaleItem;
import com.softeams.poSystem.core.repositories.SaleRepository;
import com.softeams.poSystem.core.services.interfaces.IProductService;
import com.softeams.poSystem.core.services.interfaces.ISaleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaleService implements ISaleService {
    private final SaleRepository saleRepository;
    private final IProductService productService;
    private final InventoryEntryService inventoryEntryService;
    //CRUD

    //CREATE

    public Sale createSale(Sale sale) {
        log.info("Creating sale: {}", sale.getSaleDate());
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

    public List<Sale> getTop3SalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching top 3 sales by date range: {} to {}", startDate, endDate);
        Pageable topThree = PageRequest.of(0, 3);
        return saleRepository.findTop3BySaleDateRange(startDate, endDate, topThree);
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

        //Asignar el balance al cliente si es una venta a crédito
        if (sale.isCreditSale() && sale.getClient() != null) {
            log.info("Updating client balance for credit sale");
            sale.getClient().setBalance(sale.getClient().getBalance().add(totalAmount));
        }

        //Crear la venta en la base de datos
        Sale createdSale = createSale(sale);
        log.info("Sale created successfully with id: {}", createdSale.getId());
        return new SaleResponse(
                createdSale.getId(),
                createdSale.getClient().getName(),
                createdSale.getVendedorName(),
                createdSale.getSaleDate(),
                createdSale.getTotal(),
                createdSale.isCreditSale(),
                createdSale.getItems().stream()
                        .map(item -> new SaleItemResponse(
                                item.getProduct().getSKU(),
                                item.getProduct().getNombre(),
                                item.getQuantity(),
                                item.getPrice()
                        )).collect(Collectors.toSet()),
                createdSale.getItemCount()
        );
    }
    public BigDecimal calcularCostoRealVentaFIFO(Product producto, int cantidadVendida) {
        List<InventoryEntry> entradas = inventoryEntryService.getEntriesByProductAndEntryDateAsc(producto);
        int restantes = cantidadVendida;
        BigDecimal costoTotal = BigDecimal.ZERO;

        for (InventoryEntry entry : entradas) {
            int disponibles = entry.getUnidadesAgregadas() - entry.getUnidadesVendidas();

            if (disponibles <= 0) continue;

            int usar = Math.min(restantes, disponibles);

            BigDecimal costoUnidad = entry.getPrecioPorCaja()
                    .divide(BigDecimal.valueOf(producto.getUnidadesPorPresentacion()), 4, RoundingMode.HALF_UP);

            BigDecimal costo = costoUnidad.multiply(BigDecimal.valueOf(usar));
            costoTotal = costoTotal.add(costo);

            restantes -= usar;
            if (restantes == 0) break;
        }

        if (restantes > 0) {
            throw new RuntimeException("Inventario insuficiente para calcular costo real de venta");
        }

        return costoTotal;
    }

    public BigDecimal calcularUtilidadConFIFO(SaleItem item) {
        Product producto = item.getProduct();
        int cantidadVendida = item.getQuantity();
        BigDecimal precioUnitarioAplicado = item.getPrice();

        // 1. Costo real total usando FIFO
        BigDecimal costoTotal = calcularCostoRealVentaFIFO(producto, cantidadVendida);

        // 2. Ingreso total
        BigDecimal ingreso = precioUnitarioAplicado.multiply(BigDecimal.valueOf(cantidadVendida));

        // 3. Utilidad
        return ingreso.subtract(costoTotal);
    }


    public BigDecimal calcularUtilidadVenta(Sale venta) {
        return venta.getItems().stream()
                .map(this::calcularUtilidadConFIFO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }



}
