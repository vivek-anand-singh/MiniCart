package com.example.minicart.controllers;


import com.example.minicart.dto.ProductResponseDto;
import com.example.minicart.models.Product;
import com.example.minicart.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController
{
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts()
    {
        List<Product> products = productService.getAllProducts();
        List<ProductResponseDto> response = ProductResponseDto.from(products);
        return ResponseEntity.ok(response);
    }
}