package com.example.minicart.services;

import com.example.minicart.dto.CartAddRequestDto;
import com.example.minicart.dto.CartTotalResponseDto;
import com.example.minicart.dto.CartUpdateRequestDto;
import com.example.minicart.models.CartItem;

public interface CartService
{
    public CartItem addProduct(CartAddRequestDto cartRequestDto);
    public void updateProduct(long productId, CartUpdateRequestDto cartUpdateRequestDto);
    public CartTotalResponseDto cartTotalResponse();
}
