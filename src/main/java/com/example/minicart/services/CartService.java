package com.example.minicart.services;

import com.example.minicart.dto.CartAddRequestDto;
import com.example.minicart.dto.CartUpdateRequestDto;
import com.example.minicart.models.CartItem;

public interface CartService
{
    CartItem addProduct(CartAddRequestDto cartRequestDto);
    CartItem updateProduct(CartUpdateRequestDto cartUpdateRequestDto);
    
}
