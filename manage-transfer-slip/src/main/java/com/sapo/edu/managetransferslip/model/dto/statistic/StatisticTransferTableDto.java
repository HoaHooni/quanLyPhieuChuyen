package com.sapo.edu.managetransferslip.model.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticTransferTableDto {
    private String code;
    private Date date;
    private String inventoryInput, inventoryOutput, username;
    private int productNumber;
    private Double price;
}
