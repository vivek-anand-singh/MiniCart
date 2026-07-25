package com.example.minicart.dto;

import com.example.minicart.models.CartItem;
import com.example.minicart.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponse
{
    private Product product;
    private long quantity;

    public static CartItemResponse from(CartItem cartItem)
    {
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setProduct(cartItem.getProduct());
        cartItemResponse.setQuantity(cartItemResponse.getQuantity());
        return cartItemResponse;
    }
}
