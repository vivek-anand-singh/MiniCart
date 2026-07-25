package com.example.minicart.dto;

import com.example.minicart.models.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartItemResponse
{
    private long productId;
    private String name;
    private String unit;
    private long paise;
    private long quantity;

    public static CartItemResponse from(CartItem cartItem)
    {
        CartItemResponse r = new CartItemResponse();
        r.setProductId(cartItem.getProduct().getId());
        r.setName(cartItem.getProduct().getName());
        r.setUnit(cartItem.getProduct().getUnit());
        r.setPaise(cartItem.getProduct().getPaise());
        r.setQuantity(cartItem.getQuantity());
        return r;
    }

    public static List<CartItemResponse> from(List<CartItem> cartItems)
    {
        List<CartItemResponse> cartItemResponses = new ArrayList<>();
        for(CartItem cartItem: cartItems)
        {
            cartItemResponses.add(from(cartItem));
        }
        return cartItemResponses;
    }
}
