package com.softeams.poSystem.core.mappers;


import com.softeams.poSystem.core.dtos.ProductRequest;
import com.softeams.poSystem.core.entities.Product;

import java.util.List;

public interface IProductMapper {
    Product toEntity(ProductRequest product);
    List<Product> toEntity(List<ProductRequest> product);
}
