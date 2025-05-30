package com.softeams.poSystem.core.services.interfaces;

import com.softeams.poSystem.core.entities.Product;
import com.softeams.poSystem.core.entities.SaleItem;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IProductService {
    Product getProductById(Long id);
    void updateStockAfterSale(Set<SaleItem> products);
}
