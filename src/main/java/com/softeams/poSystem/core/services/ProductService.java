package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.dtos.product.AltaProduct;
import com.softeams.poSystem.core.dtos.product.ProductResponse;
import com.softeams.poSystem.core.entities.Product;
import com.softeams.poSystem.core.entities.SaleItem;
import com.softeams.poSystem.core.mappers.ProductMapper;
import com.softeams.poSystem.core.repositories.ProductRepository;
import com.softeams.poSystem.core.services.interfaces.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
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
        return productRepository.findByNombreContainingIgnoreCaseOrMarcaContainingIgnoreCaseOrSKUContainingIgnoreCase(query,query,query)
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .toList();
    }

    public Long getProductCount() {
        log.info("Fetching product count");
        return productRepository.countByIsActiveTrue();
    }

    public Long getLowStockCount(int threshold) {
        log.info("Fetching low stock count for threshold: {}", threshold);
        return productRepository.countByStockLessThanAndIsActiveTrue(threshold);
    }

    //UPDATE
    @Transactional
    public ProductResponse updateProduct(Product dto, Long id) {
        log.info("Updating product with id: {}", dto.getNombre());
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setSKU(dto.getSKU());
        product.setNombre(dto.getNombre());
        product.setMarca(dto.getMarca());
        product.setGradosAlcohol(dto.getGradosAlcohol());
        product.setTamanio(dto.getTamanio());
        product.setPrecioVenta(dto.getPrecioNormal());
        product.setPrecioMayoreo(dto.getPrecioMayoreo());
        product.setStock(dto.getStock());
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
        productRepository.save(product);
    }

    //LOGIC

    @Transactional
    public void updateStockAfterSale(Set<SaleItem> products) {
        for(SaleItem item : products) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + item.getProduct().getId()));
            int newStock = product.getStock() - item.getQuantity();
            if (newStock < 0) {
                throw new RuntimeException("Insufficient stock for product: " + product.getNombre());
            }
            product.setStock(newStock);
            productRepository.save(product);
            log.info("Updated stock for product: {}. New stock: {}", product.getNombre(), newStock);
        }
    }

    //Alta product
    @Transactional
    public String altaProducts(List<AltaProduct> altas){

        try{
            for(AltaProduct alta : altas) {
                Product existingProduct = productRepository.findBySKU(alta.sku());
                existingProduct.setStock(existingProduct.getStock() + alta.cantidad());
                productRepository.save(existingProduct);
            }
        }catch (Exception e){
            log.error("Error al procesar las altas de productos: {}", e.getMessage());
            return "Error al procesar las altas de productos: " + e.getMessage();
        }
        return "Altas de productos procesadas correctamente.";
    }

}
