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
public class StatisticProductFormDto {

    private Date dateStart = null;
    private Date dateEnd = null;
    private List<Integer> inventory = new ArrayList<>();

    PaginationDto page = new PaginationDto();
    SortDto sort = new SortDto();
}
