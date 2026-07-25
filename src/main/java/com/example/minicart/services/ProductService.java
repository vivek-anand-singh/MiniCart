package com.example.minicart.services;

import com.example.minicart.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService
{
    public List<Product> getAllProducts();
}
