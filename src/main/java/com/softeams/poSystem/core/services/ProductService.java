package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.dtos.product.AltaProduct;
import com.softeams.poSystem.core.dtos.product.ProductResponse;
import com.softeams.poSystem.core.entities.InventoryEntry;
import com.softeams.poSystem.core.entities.Product;
import com.softeams.poSystem.core.entities.SaleItem;
import com.softeams.poSystem.core.mappers.ProductMapper;
import com.softeams.poSystem.core.repositories.ProductRepository;
import com.softeams.poSystem.core.services.interfaces.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final InventoryEntryService inventoryEntryService;
    //CRUD

    //CREATE
    @Transactional
    public Product createProduct(Product product) {
        log.info("Creating product: {}", product);
        return productRepository.save(product);
    }

    public List<Product> createProducts(List<Product> products) {
        log.info("Creating products: {}", products);
        return productRepository.saveAll(products);
    }

    //READ
    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findByIsActiveTrue()
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .toList();
    }

    public Product getProductById(Long id) {
        log.info("Fetching product by id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public List<Product> getProductsByMarcaOrNombreOrSKU(String query) {
        log.info("Fetching products by brand or name: {}", query);
        return productRepository.buscarActivosPorNombreOSku(query)
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .toList();
    }

    public Product getProductBySKU(String sku) {
        log.info("Fetching product by SKU: {}", sku);
        return productRepository.findBySKU(sku);
    }

    public Long getProductCount() {
        log.info("Fetching product count");
        return productRepository.countByIsActiveTrue();
    }

    public Long getLowStockCount(int threshold) {
        log.info("Fetching low stock count for threshold: {}", threshold);
        return productRepository.countByStockLessThanAndIsActiveTrue(threshold);
    }

    public BigDecimal calcularCostoInventarioPorProducto(Long id){
        log.info("");
        return inventoryEntryService.calcularValorInventarioDisponiblePorProducto(id);
    }

    public BigDecimal calculatCostoInventarioTotal(){
        return inventoryEntryService.calcularValorInventarioDisponible();
    }

    //UPDATE
    @Transactional
    public ProductResponse updateProduct(Product dto, Long id) {
        log.info("Updating product with id: {}", dto.getNombre());
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        BigDecimal precioPiezaMayoreo = dto.getPrecioMayoreo()
                .divide(
                        new BigDecimal(dto.getUnidadesPorPresentacion()),
                        2, // scale: number of decimal places
                        BigDecimal.ROUND_HALF_UP // rounding mode
                );
        BigDecimal stockPorUnidades = dto.getStock()
                .multiply(BigDecimal.valueOf(dto.getUnidadesPorPresentacion()));


        product.setSKU(dto.getSKU());
        product.setNombre(dto.getNombre());
        product.setPrecioCosto(dto.getPrecioCosto());
        product.setPrecioVenta(dto.getPrecioVenta());
        product.setPrecioMayoreo(dto.getPrecioMayoreo());
        product.setPrecioPiezaVenta(dto.getPrecioPiezaVenta());
        product.setStock(dto.getStock());
        product.setUnidadesPorPresentacion(dto.getUnidadesPorPresentacion());
        product.setStockMinimo(dto.getStockMinimo());
        product.setMinimoMayoreo(dto.getMinimoMayoreo());
        product.setPrecioPiezaMayoreo(precioPiezaMayoreo);
        product.setStockPorUnidades(stockPorUnidades.intValue());
        product.setImagePath(dto.getImagePath());

        Product updated = productRepository.save(product);

        return productMapper.toDto(updated);
    }

    //DELETE
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        product.setIsActive(false);
        product.setSKU(product.getSKU()+"-DELETED");
        productRepository.save(product);
    }

    //LOGIC

    @Transactional
    public void updateStockAfterSale(Set<SaleItem> products) {
        for (SaleItem item : products) {
            int quantityToDeduct = item.getQuantity();

            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + item.getProduct().getId()));

            // 1. Obtener todas las entradas disponibles del producto (orden FIFO)
            List<InventoryEntry> entries = inventoryEntryService
                    .getEntriesByProductAndEntryDateAsc(product);

            int remaining = quantityToDeduct;
            for (InventoryEntry entry : entries) {
                int disponibles = entry.getUnidadesDisponibles();

                if (disponibles <= 0) continue;

                int cantidadARestar = Math.min(disponibles, remaining);
                entry.setUnidadesVendidas(entry.getUnidadesVendidas() + cantidadARestar);
                inventoryEntryService.save(entry);

                remaining -= cantidadARestar;

                if (remaining <= 0) break;
            }
            if (remaining > 0) {
                throw new RuntimeException("Insufficient inventory in InventoryEntry for product: " + product.getNombre());
            }

            //Update stockPorUnidades
            Integer newStock = product.getStockPorUnidades() - item.getQuantity();
            if (newStock < 0) {
                throw new RuntimeException("Insufficient stock for product: " + product.getNombre());
            }

            // Update stock
            BigDecimal newStockDecimal = BigDecimal.valueOf(newStock)
                    .divide(BigDecimal.valueOf(product.getUnidadesPorPresentacion()), 2, RoundingMode.HALF_UP);

            product.setStock(newStockDecimal);
            product.setStockPorUnidades(newStock);
            productRepository.save(product);
            log.info("Updated stock for product: {}. New stock: {}", product.getNombre(), newStock);
        }
    }

    //Alta product
    @Transactional
    public String altaProducts(List<AltaProduct> altas) {
        try {
            for (AltaProduct alta : altas) {
                var product = productRepository.findBySKU(alta.sku());
                InventoryEntry entrada = inventoryEntryService.registrarEntradaInventario(alta, product);

                int unidadesAgregadas = entrada.getUnidadesAgregadas();

                Integer newStock = product.getStockPorUnidades() + unidadesAgregadas;

                BigDecimal newStockDecimal = BigDecimal.valueOf(newStock)
                        .divide(BigDecimal.valueOf(product.getUnidadesPorPresentacion()), 2, RoundingMode.HALF_UP);

                product.setStock(newStockDecimal);
                product.setStockPorUnidades(newStock);
                productRepository.save(product);
            }
        } catch (Exception e) {
            log.error("Error al procesar las altas de productos: {}", e.getMessage());
            return "Error al procesar las altas de productos: " + e.getMessage();
        }
        return "Altas de productos procesadas correctamente.";
    }

    public void createInventoryEntry(Product product) {
        inventoryEntryService.save(
                InventoryEntry.builder()
                        .entryDate(LocalDateTime.now())
                        .producto(product)
                        .cajasCompradas(product.getStock().intValue())
                        .precioPorCaja(product.getPrecioCosto())
                        .unidadesVendidas(0)
                        .build()
        );
    }


}
