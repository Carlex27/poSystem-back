package com.softeams.poSystem.core.mappers;


import com.softeams.poSystem.core.dtos.ProductRequest;
import com.softeams.poSystem.core.entities.Product;

public interface IProductMapper {
    Product toEntity(ProductRequest product);
}
