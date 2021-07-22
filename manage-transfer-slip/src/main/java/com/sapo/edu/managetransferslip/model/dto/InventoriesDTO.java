package com.sapo.edu.managetransferslip.model.dto;

import com.sapo.edu.managetransferslip.model.entity.InventoriesEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoriesDTO {

    private int id;

    private String code;

    private String name;

    private String address;

    private String phone;

    private String mail;

    private Timestamp createAt;

    private Timestamp updateAt;

    private Timestamp deletedAt;

     @Override
    public String toString() {
        return "InventoriesDTO{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", mail='" + mail + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", deletedAt=" + deletedAt +
                '}';
    }

    public InventoriesDTO(InventoriesEntity inventory) {
        this.id = inventory.getId();
        this.code = inventory.getCode();
        this.name = inventory.getName();
        this.address = inventory.getAddress();
        this.phone = inventory.getPhone();
        this.mail = inventory.getMail();
        this.createAt = inventory.getCreateAt();
        this.updateAt = inventory.getUpdateAt();
        this.deletedAt = inventory.getDeletedAt();
    }
}
