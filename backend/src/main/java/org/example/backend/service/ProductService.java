package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.repository.ProductRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final IdService idService;


}
