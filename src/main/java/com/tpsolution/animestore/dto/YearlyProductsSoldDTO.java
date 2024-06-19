package com.tpsolution.animestore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearlyProductsSoldDTO {
    private int year;
    private int productsSold;
}
