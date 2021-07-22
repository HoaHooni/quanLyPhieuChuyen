package com.sapo.edu.managetransferslip.model.dto;

import com.sapo.edu.managetransferslip.model.entity.TransferDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
public class TransferDetailDTO {

    private int id;

//    private TransferDTO transfer;
//
//    private ProductsDTO product;
    private String code;

    private String productName;

    private int total;

    public  TransferDetailDTO(){}
    public TransferDetailDTO(TransferDetailEntity transferDetailEntity){
        this.id = transferDetailEntity.getId();
        this.code = transferDetailEntity.getProduct().getCode();
        this.productName= transferDetailEntity.getProduct().getName();
        this.total=transferDetailEntity.getTotal();

    }


}
