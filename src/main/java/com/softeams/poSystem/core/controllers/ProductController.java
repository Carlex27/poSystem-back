package com.softeams.poSystem.core.controllers;


import com.softeams.poSystem.core.dtos.AltaProduct;
import com.softeams.poSystem.core.dtos.ProductRequest;
import com.softeams.poSystem.core.entities.Product;
import com.softeams.poSystem.core.mappers.IProductMapper;

import com.softeams.poSystem.core.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final IProductMapper productMapper;
    //CRUD operations can be added here as endpoints
    //CREATE

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @ModelAttribute ProductRequest form
            ) {
        Product product = productMapper.toEntity(form);

        MultipartFile image = form.getImagen();
        if (image != null && !image.isEmpty()) {
            try {
                String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path imagePath = Paths.get("uploads/images/" + imageName);
                Files.createDirectories(imagePath.getParent());
                Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                product.setImagePath("/images/" + imageName);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la imagen");
            }
        }

        log.info("[ProductController | CreateProduct] Creating product: {}", product.getNombre());
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("/createMany")
    public ResponseEntity<?> createProducts(
            @Valid
            @RequestBody List<ProductRequest> productsRequest
    ) {
        log.info("[ProductController | CreateProducts] Creating product");
        List<Product> products = productMapper.toEntity(productsRequest);
        return ResponseEntity.ok(productService.createProducts(products));
    }


    //READ
    @PreAuthorize("hasAuthority('SCOPE_READ') or hasAuthority('SCOPE_READ_VENDEDOR')")
    @GetMapping("/findAll")
    public ResponseEntity<?> getAllProducts(
            Authentication authentication
    ) {
        log.info("[ProductController | GetAllProducts] Fetching all products by: {}", authentication.getName());
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PreAuthorize("hasAuthority('SCOPE_READ') or hasAuthority('SCOPE_READ_VENDEDOR')")
    @GetMapping("/findById/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        log.info("[ProductController | GetProductById] Fetching product by id: {} by: {}", id, authentication.getName());
        return ResponseEntity.ok(productService.getProductById(id));
    }


    @GetMapping("/search")
    public ResponseEntity<?> getProductsByMarca(
            @RequestParam String query,
            Authentication authentication
    ) {
        log.info("[ProductController | GetProductsByMarca] Fetching products by brand: {} by: {}", query, authentication.getName());
        return ResponseEntity.ok(productService.getProductsByMarcaOrNombreOrSKU(query));
    }

    //UPDATE
    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(
            @ModelAttribute ProductRequest form,
            @RequestParam Long id,
            Authentication authentication
    ) {
        log.info("[ProductController | UpdateProduct] Updating product by: {}", authentication.getName());

        Product product = productService.getProductById(id);
        MultipartFile image = form.getImagen();
        if (image != null && !image.isEmpty()) {
            try {
                String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path imagePath = Paths.get("uploads/images/" + imageName);
                Files.createDirectories(imagePath.getParent());
                Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                product.setImagePath("/images/" + imageName);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar imagen.");
            }
        }


        Product nProduct = productMapper.toEntity(form);
        nProduct.setImagePath(product.getImagePath());

        return ResponseEntity.ok(productService.updateProduct(nProduct,id));
    }

    //DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long id,
            Authentication authentication
    ) {
        log.info("[ProductController | DeleteProduct] Deleting product by id: {} by: {}", id, authentication.getName());
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }


    //Imagenes
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        Path path = Paths.get("uploads/images/" + filename);
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException("No se puede leer la imagen");
        }

        // Detectar tipo MIME automáticamente
        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream"; // valor por defecto
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    //Alta producto
    @PostMapping("/altaProducto")
    public ResponseEntity<?> altaProducto(
            @Valid
            @RequestBody List<AltaProduct> altas,
            Authentication authentication
    ) {
        log.info("[ProductController | AltaProducto] Alta de producto por: {}", authentication.getName());
        return ResponseEntity.ok(productService.altaProducts(altas));
    }

}
