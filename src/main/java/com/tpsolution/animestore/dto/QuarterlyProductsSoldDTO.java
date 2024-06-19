package com.tpsolution.animestore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuarterlyProductsSoldDTO {
    private int productsSold;
    private int year;
    private int quarter;
}
