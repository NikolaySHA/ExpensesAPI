package com.NikolaySHA.expenses_api.model.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OutExpenseDTO {
    private long id;
    private String name;
    private double price;
}
