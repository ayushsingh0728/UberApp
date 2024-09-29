package com.project.uber.uberApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RazorpayOrderRequestDto {

    private Double amount;

    private String currency;

    private String receiptId;

}
