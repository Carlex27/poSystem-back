package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.dtos.ProductResponse;
import com.softeams.poSystem.core.dtos.ProductRequest;
import com.softeams.poSystem.core.entities.Product;
import com.softeams.poSystem.core.mappers.ProductMapper;
import com.softeams.poSystem.core.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
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
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        log.info("Fetching product by id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    public Product getProductByName(String name) {
        log.info("Fetching product by name: {}", name);
        return productRepository.findByNombre(name)
                .orElseThrow(() -> new RuntimeException("Product not found with name: " + name));
    }
    public List<Product> getProductsByMarca(String marca) {
        log.info("Fetching products by brand: {}", marca);
        return productRepository.findAllByMarca(marca);
    }

    //UPDATE
    @Transactional
    public ProductResponse updateProduct(ProductRequest dto) {
        log.info("Updating product with id: {}", dto.nombre());
        Product product = productRepository.findByNombre(dto.nombre())
                .orElseThrow(() -> new RuntimeException("Product not found with name: " + dto.nombre()));

        product.setNombre(dto.nombre());
        product.setMarca(dto.marca());
        product.setGradosAlcohol(dto.gradosAlcohol());
        product.setTamanio(dto.tamanio());
        product.setPrecioNormal(dto.precioNormal());
        product.setPrecioMayoreo(dto.precioMayoreo());
        product.setStock(dto.stock());

        Product updated = productRepository.save(product);

        return productMapper.toDto(updated);
    }

    //DELETE
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(product);
    }
}
