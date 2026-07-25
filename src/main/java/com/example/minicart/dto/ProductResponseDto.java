package com.example.minicart.dto;

import com.example.minicart.models.Product;
import com.example.minicart.repositories.ProductRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductResponseDto
{
    private String name;
    private long paise;
    private String unit;

    public static List<ProductResponseDto> from(List<Product> list)
    {
        List<ProductResponseDto> responseDtos = new ArrayList<>();
        for(Product p: list)
        {
            responseDtos.add(convert(p));
        }
        return responseDtos;
    }

    private static ProductResponseDto convert(Product product)
    {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setName(product.getName());
        productResponseDto.setPaise(productResponseDto.getPaise());
        productResponseDto.setUnit(productResponseDto.getUnit());

        return productResponseDto;
    }
}
