package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.dtos.product.AltaProduct;
import com.softeams.poSystem.core.entities.InventoryEntry;
import com.softeams.poSystem.core.entities.Product;
import com.softeams.poSystem.core.repositories.InventoryEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryEntryService {
    private final InventoryEntryRepository inventoryEntryRepository;


    public InventoryEntry registrarEntradaInventario(AltaProduct altaProduct, Product product){
        log.info("Registrando entrada de inventario para el producto: {}", altaProduct);

        InventoryEntry entrada = InventoryEntry.builder()
                .producto(product)
                .entryDate(LocalDateTime.now())
                .cajasCompradas(altaProduct.cantidad())
                .precioPorCaja(altaProduct.precioCosto())
                .unidadesVendidas(0)
                .build();

        // Guardar la entrada de inventario
        return inventoryEntryRepository.save(entrada);

    }

    public List<InventoryEntry> getEntriesByProductAndEntryDateAsc(Product product) {
        log.info("Obteniendo entradas de inventario para el producto: {}", product.getNombre());
        return inventoryEntryRepository.findByProductoOrderByEntryDateAsc(product);
    }
    public List<InventoryEntry> getEntriesByProductIdAndEntryDateAsc(Long id) {
        log.info("Obteniendo entradas de inventario para el producto: {}", id);
        return inventoryEntryRepository.findByProductoIdOrderByEntryDateAsc(id);
    }


    public void save(InventoryEntry inventoryEntry) {
        log.info("Guardando entrada de inventario: {}", inventoryEntry);
        inventoryEntryRepository.save(inventoryEntry);
    }

    public BigDecimal calcularValorInventarioDisponiblePorProducto(Long productId){
        return inventoryEntryRepository.calcularValorInventarioDisponiblePorProducto(productId);
    }

    public BigDecimal calcularValorInventarioDisponible(){
        return inventoryEntryRepository.calcularValorInventarioDisponible();
    }

    public void update(Long id, InventoryEntry entryNew){
        InventoryEntry entry = inventoryEntryRepository.findById(id)
                .orElseThrow();

        entry.setCajasCompradas(entryNew.getCajasCompradas());
        entry.setPrecioPorCaja(entryNew.getPrecioPorCaja());
        inventoryEntryRepository.save(entry);
    }
}
