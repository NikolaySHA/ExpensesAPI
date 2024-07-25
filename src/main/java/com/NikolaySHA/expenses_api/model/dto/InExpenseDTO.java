package com.NikolaySHA.expenses_api.model.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InExpenseDTO {
    
    private String name;
    private double price;
    private long appointmentId;
}
