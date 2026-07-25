package com.example.minicart.dto;

import com.example.minicart.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponse
{
    private Product product;
    private long quantity;
}
