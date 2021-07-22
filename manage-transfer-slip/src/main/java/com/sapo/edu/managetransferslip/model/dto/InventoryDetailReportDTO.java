package com.sapo.edu.managetransferslip.model.dto;


import com.sapo.edu.managetransferslip.model.entity.InventoriesEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InventoryDetailReportDTO {

    // TODO: 7/14/2021
    private InventoriesDTO inventoriesDTO;
    private List<CustomInventoriesDetails> customInventoriesDetails ;


    public InventoryDetailReportDTO(InventoriesDTO inventoriesDTO, List<CustomInventoriesDetails> customInventoriesDetails) {
        this.inventoriesDTO = inventoriesDTO;
        this.customInventoriesDetails = customInventoriesDetails;

    }

    @Override
    public String toString() {
        return "InventoryDetailReportDTO{" +
                "inventoriesDTO=" + inventoriesDTO +
                ", customInventoriesDetails=" + customInventoriesDetails +
                '}';
    }
}
