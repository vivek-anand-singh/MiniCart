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
    private long id;
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

    public static ProductResponseDto convert(Product product)
    {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPaise(product.getPaise());
        dto.setUnit(product.getUnit());
        return dto;
    }
}
