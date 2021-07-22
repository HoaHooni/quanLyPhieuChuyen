package com.sapo.edu.managetransferslip.model.dto.statistic;

import com.sapo.edu.managetransferslip.model.dto.PaginationDto;
import com.sapo.edu.managetransferslip.model.dto.SortDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticInventoryFormDto {
    private PaginationDto page = new PaginationDto(0, 1, 3);
    private SortDto sort = new SortDto();
    private List<Integer> inventory = new ArrayList<>();
    private Date date;
    private int user;
}
