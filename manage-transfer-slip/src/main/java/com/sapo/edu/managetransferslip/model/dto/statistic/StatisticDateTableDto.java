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
public class StatisticDateTableDto {

    private Date createDate;
    private int transferImport, transferExport;
    private int productImport, productExport;
    private double proTotalImport, proTotalExport;

}
