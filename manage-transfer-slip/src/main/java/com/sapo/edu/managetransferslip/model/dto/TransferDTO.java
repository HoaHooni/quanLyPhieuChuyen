package com.sapo.edu.managetransferslip.model.dto;

import com.sapo.edu.managetransferslip.model.entity.TransferDetailEntity;
import com.sapo.edu.managetransferslip.model.entity.TransferEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class  TransferDTO {

    private int id;

    private String code;

    private String note;

    private String status;

    private Timestamp createAt;

    private Timestamp movingAt;

    private Timestamp finishAt;

    private Timestamp deletedAt;

    private String username;

    private  int inventoryInputId;

    private String inventoryInputName;

    private int inventoryOutputId;

    private String inventoryOutputName;

    private List<TransferDetailDTO> transferDetailDTOList;

    public TransferDTO(){}
    public TransferDTO(TransferEntity transferEntity){
        this.id = transferEntity.getId();
        this.code = transferEntity.getCode();
        this.note = transferEntity.getNote();
        if(transferEntity.getStatus() == 1){
            this.status = "Chờ chuyển";
        }
        else if(transferEntity.getStatus() == 2){
            this.status ="Đang chuyển";
        }
        else if(transferEntity.getStatus() == 3){
            this.status ="Đã nhận";
        }
        else if(transferEntity.getStatus() == 4){
            this.status ="Đã hủy";
        }

        this.createAt= transferEntity.getCreateAt();
        this.movingAt = transferEntity.getMovingAt();
        this.finishAt = transferEntity.getFinishAt();
        this.deletedAt= transferEntity.getDeletedAt();
        this.username= transferEntity.getUser().getUsername();
        this.inventoryInputId = transferEntity.getInventoryInput().getId();
        this.inventoryInputName = transferEntity.getInventoryInput().getName();
        this.inventoryOutputId = transferEntity.getInventoryOutput().getId();
        this.inventoryOutputName = transferEntity.getInventoryOutput().getName();
        List<TransferDetailDTO> transferDetailDTOS = new ArrayList<>();
        for(TransferDetailEntity transferDetailEntity: transferEntity.getTransferDetailEntities()){
            TransferDetailDTO transferDetailDTO = new TransferDetailDTO(transferDetailEntity);
            transferDetailDTOS.add(transferDetailDTO);
        }
        this.transferDetailDTOList = transferDetailDTOS ;
    }



}
