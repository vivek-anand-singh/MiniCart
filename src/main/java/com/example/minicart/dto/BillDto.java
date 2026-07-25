package com.example.minicart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillDto
{
    private long totalPaise;
    private long deliveryFeePaise;
    private long grandTotalPaise;
}
