package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.dtos.ProductResponse;
import com.softeams.poSystem.core.dtos.ProductRequest;
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

    public List<Product> getProductsByMarcaOrNombre(String query) {
        log.info("Fetching products by brand or name: {}", query);
        return productRepository.findByNombreContainingIgnoreCaseOrMarcaContainingIgnoreCase(query,query)
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .toList();
    }

    //UPDATE
    @Transactional
    public ProductResponse updateProduct(Product dto) {
        log.info("Updating product with id: {}", dto.getNombre());
        Product product = productRepository.findByNombre(dto.getNombre())
                .orElseThrow(() -> new RuntimeException("Product not found with name: " + dto.getNombre()));

        product.setSKU(dto.getSKU());
        product.setNombre(dto.getNombre());
        product.setMarca(dto.getMarca());
        product.setGradosAlcohol(dto.getGradosAlcohol());
        product.setTamanio(dto.getTamanio());
        product.setPrecioNormal(dto.getPrecioNormal());
        product.setPrecioMayoreo(dto.getPrecioMayoreo());
        product.setStock(dto.getStock());

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
}
