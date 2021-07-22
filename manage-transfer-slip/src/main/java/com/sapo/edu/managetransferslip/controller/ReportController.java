package com.sapo.edu.managetransferslip.controller;

import com.sapo.edu.managetransferslip.model.dto.statistic.*;
import com.sapo.edu.managetransferslip.service.IReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Stack;

@RestController
@PreAuthorize("hasRole('ROLE_MANAGER')")
@CrossOrigin("http://localhost:3006")
@RequestMapping("admin/statistic")
public class ReportController {

    private IReportService reportService;

    public ReportController(IReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/product")
    public ResponseEntity getSatisticProduct(@RequestBody(required = false) StatisticProductFormDto searchForm) {
        List<StatisticProductTableDto> list = reportService.getSatisticProduct(searchForm);
        Stack data = new Stack();
        data.push(list);
        data.push(searchForm.getPage().getCount());
        return ResponseEntity.ok(data);
    }

    @PostMapping("/transfer")
    public ResponseEntity getSatisticTransfer(@RequestBody(required = false) StatisticTransferFormDto statisticTransferFormDto) {

            List<StatisticTransferTableDto> list = reportService.getStatisticTransfer(statisticTransferFormDto);
            return ResponseEntity.ok(list);
    }

    @PostMapping("/date")
    public ResponseEntity getSatisticDate(@RequestBody(required = false) StatisticDateFormDto statisticTransferFormDto) {

        List<StatisticDateTableDto> list = reportService.getStatisticDate(statisticTransferFormDto);

        Stack data = new Stack();
        data.push(list);
        data.push(statisticTransferFormDto.getPage().getCount());
        return ResponseEntity.ok(data);
    }

    @PostMapping("/inventory")
    public ResponseEntity getInventory(@RequestBody(required = false) StatisticInventoryFormDto statisticInventoryFormDto){
        Stack data = new Stack();
        List<StatisticInventoryTableDto> list  = reportService.getSatisticInventory(statisticInventoryFormDto);
        data.push(list);
        data.push(statisticInventoryFormDto.getPage().getCount());

        return ResponseEntity.ok(data);
    }

}
