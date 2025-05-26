package com.softeams.poSystem.core.controllers;

import com.softeams.poSystem.core.dtos.ProductRequest;
import com.softeams.poSystem.core.mappers.IProductMapper;
import com.softeams.poSystem.core.mappers.ProductMapper;
import com.softeams.poSystem.core.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final IProductMapper productMapper;
    //CRUD operations can be added here as endpoints
    //CREATE

    public ResponseEntity<?> createProduct(
            @Valid
            @RequestBody ProductRequest productRequest
            ) {
        log.info("[ProductController | CreateProduct] Creating product");
        return ResponseEntity.ok(productService.createProduct(productMapper.toEntity(productRequest)));
    }
    //READ
    //UPDATE
    //DELETE

}
