package com.example.minicart.controllers;

import com.example.minicart.dto.CartAddRequestDto;
import com.example.minicart.dto.CartItemResponse;
import com.example.minicart.dto.CartUpdateRequestDto;
import com.example.minicart.models.CartItem;
import com.example.minicart.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController
{
    private CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addProduct(@RequestBody CartAddRequestDto cartAddRequestDto)
    {
        CartItem cartItem = cartService.addProduct(cartAddRequestDto);
        return ResponseEntity.ok(CartItemResponse.from(cartItem));
    }

    @PatchMapping("/cart/items/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable long productId, @RequestBody CartUpdateRequestDto cartUpdateRequestDto)
    {
        cartService.updateProduct(productId,cartUpdateRequestDto);
        return ResponseEntity.noContent().build();
    }

    
}
