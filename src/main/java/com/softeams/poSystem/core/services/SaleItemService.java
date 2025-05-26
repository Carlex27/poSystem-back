package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.repositories.SaleItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaleItemService {
    private final SaleItemRepository saleItemRepository;
}
