package com.example.minicart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillDto
{
    private long itemTotalPaise;
    private long deliveryFeePaise;
    private long grandTotalPaise;
}
