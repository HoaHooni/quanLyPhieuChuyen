package com.sapo.edu.managetransferslip.model.dto;

import com.sapo.edu.managetransferslip.model.entity.HistoryEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class HistoryDTO {
     //ma kho nhap
     //ten kho nhap
     private InventoriesDTO inventoriesDTOImport;
     //ma kho xuat
     //ten kho xuat
     private  InventoriesDTO inventoriesDTOExport;
     //so luong san pham trong kho thieu
     //chi tiet so luong san pham thuoc kho
     //ma
     private TransferDTO transferDTO;
     private String action;
     private Timestamp date;
     private UsersDTO usersDTO;

     public HistoryDTO(InventoriesDTO inventoriesDTOImport, InventoriesDTO inventoriesDTOExport, TransferDTO transferDTO, String action, Timestamp date, UsersDTO usersDTO) {
          this.inventoriesDTOImport = inventoriesDTOImport;
          this.inventoriesDTOExport = inventoriesDTOExport;
          this.transferDTO = transferDTO;
          this.action = action;
          this.date = date;
          this.usersDTO = usersDTO;
     }



     @Override
     public String toString() {
          return "HistoryDTO{" +
                  "inventoriesDTOImport=" + inventoriesDTOImport +
                  ", inventoriesDTOExport=" + inventoriesDTOExport +
                  ", transferDTO=" + transferDTO +
                  ", action='" + action + '\'' +
                  ", date=" + date +
                  ", usersDTO=" + usersDTO +
                  '}';
     }
}
