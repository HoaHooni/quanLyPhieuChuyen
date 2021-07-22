package com.sapo.edu.managetransferslip.model.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StatisticProductTableDto {
    private String code;
    private String name;
    private int transferImport;
    private int transferExport;
    private Double productImport;
    private Double productExport;

}
