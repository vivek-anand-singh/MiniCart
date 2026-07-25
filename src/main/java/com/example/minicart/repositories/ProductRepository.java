package com.example.minicart.repositories;

import com.example.minicart.controllers.ProductController;
import com.example.minicart.models.Product;
import com.example.minicart.services.ProductService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>
{

}
