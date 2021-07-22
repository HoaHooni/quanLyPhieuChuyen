package com.sapo.edu.managetransferslip.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InputTransferDetailDTO {
    private int id;

    //private int transfer_id;

    private int productId;

    private int total;
}
