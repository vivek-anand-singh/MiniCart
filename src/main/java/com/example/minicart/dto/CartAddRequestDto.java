package com.example.minicart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartAddRequestDto
{
    private long productId;
    private long quantity;
}
