package com.example.minicart.repositories;

import com.example.minicart.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem,Long>
{
    Optional<CartItem> findByProductId(Long productId);
}
