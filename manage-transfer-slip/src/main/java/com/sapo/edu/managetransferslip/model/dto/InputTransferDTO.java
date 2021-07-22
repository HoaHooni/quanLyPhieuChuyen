package com.sapo.edu.managetransferslip.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class InputTransferDTO {
    private int id;

    private String code;

    private String note;

    private Date createAt;

    private Date deletedAt;

    private int user_id;

    private  int inventoryInputId;

    private int inventoryOutputId;

    private List<InputTransferDetailDTO> inputTransferDetailDTOList;
}
