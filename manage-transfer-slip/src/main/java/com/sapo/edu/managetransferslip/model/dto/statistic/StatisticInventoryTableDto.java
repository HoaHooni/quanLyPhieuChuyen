package com.sapo.edu.managetransferslip.model.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticInventoryTableDto {
    private int inventoryId;
    private String inventory;
    private int transferImport, transferExport;
    private int productImport, productExport;
    private double proTotalImport, proTotalExport;
}
