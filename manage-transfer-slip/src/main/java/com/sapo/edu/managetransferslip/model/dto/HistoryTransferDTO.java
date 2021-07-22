package com.sapo.edu.managetransferslip.model.dto;

import com.sapo.edu.managetransferslip.model.entity.HistoryEntity;
import com.sapo.edu.managetransferslip.model.entity.UsersEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
public class HistoryTransferDTO {


    private int id;


    private int transferId;


    private Timestamp date;


    private String username;


    private String action;


    private String note;

     public HistoryTransferDTO(){

    }

    public HistoryTransferDTO(HistoryEntity historyEntity){
         this.id = historyEntity.getId();
         this.transferId = historyEntity.getTransferId();
         this.date = historyEntity.getDate();
         this.username = historyEntity.getUser().getUsername();
        if(historyEntity.getAction() == 1){
            this.action = "Thêm mới phiếu chuyển";
        }
        else if(historyEntity.getAction() == 2){
            this.action ="Chuyển hàng";
        }
        else if(historyEntity.getAction() == 3){
            this.action ="Nhận hàng";
        }
        else if(historyEntity.getAction() == 4){
            this.action ="Hủy phiếu";
        }

         this.note = historyEntity.getNote();
    }


}
