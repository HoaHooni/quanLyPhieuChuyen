package com.sapo.edu.managetransferslip.service;

import com.sapo.edu.managetransferslip.model.dto.statistic.*;

import java.util.List;

public interface IReportService {
    List<StatisticProductTableDto> getSatisticProduct(StatisticProductFormDto statisticProductForm);
    List<StatisticTransferTableDto> getStatisticTransfer(StatisticTransferFormDto statisticTransferFormDto);
    List<StatisticDateTableDto> getStatisticDate(StatisticDateFormDto statisticDateFormDto);
    List<StatisticInventoryTableDto> getSatisticInventory(StatisticInventoryFormDto statisticInventoryFormDto);
     <T> int getCountPage(List<T> t);
}
