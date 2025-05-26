package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.entities.Product;
import com.softeams.poSystem.core.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
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
    public Product updateProduct(Long id, Product product) {
        log.info("Updating product with id: {}", id);
        Product existingProduct = getProductById(id);
        existingProduct.setNombre(product.getNombre());
        existingProduct.setMarca(product.getMarca());
        existingProduct.setPrecio(product.getPrecio());
        return productRepository.save(existingProduct);
    }
}
