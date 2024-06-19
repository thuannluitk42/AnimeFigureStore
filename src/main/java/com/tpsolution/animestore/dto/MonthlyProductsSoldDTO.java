package com.tpsolution.animestore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyProductsSoldDTO {
    private int year;
    private int month;
    private int productsSold;
}
