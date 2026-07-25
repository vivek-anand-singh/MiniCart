package com.example.minicart.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product extends BaseModel
{
    private String name;
    private long paise;
    private String unit;
}
