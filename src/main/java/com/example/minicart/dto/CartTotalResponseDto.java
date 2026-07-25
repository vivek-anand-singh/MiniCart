package com.example.minicart.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
public class CartTotalResponseDto
{
    List<CartItemResponse> items;
    BillDto bill;
}
