package com.example.pyjachok.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DrinkerDTO {
    private String date;
    private String time;
    private String description;
    private int countOfPeople;
    private String whoPay;
    private int budget;
    private int establishmentId;
    private String establishmentName;
}
