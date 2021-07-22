package com.sapo.edu.managetransferslip.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "inventories")
@Getter
@Setter
@NoArgsConstructor
public class InventoriesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", unique = true)
    @NotNull(message = "Inventory's code is not null")
    private String code;

    @Column(name = "name",unique = true)
    @NotNull(message = "Inventory's name is not null")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    @Pattern(regexp = "(09|03|07|08|05)+[0-9]{8,11}", message = "Phone is invalid")
    private String phone;

    @Column(name = "mail")
    @Email(message = "Email is invalid")
    @Pattern(regexp = "[a-zA-Z][a-z0-9A-Z_\\.]{5,32}@[a-zA-Z0-9]{2,}(\\.[a-zA-Z0-9]{2,4}){1,2}",message = "Invalid Email")
    private String mail;

    @Column(name = "create_at")
    private Timestamp createAt;

    @Column(name = "update_at")
    private Timestamp updateAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;


    @OneToMany(mappedBy="inventoryInput")
    private List<TransferEntity> transferEntitiesInput;


    @OneToMany(mappedBy="inventoryOutput")
    private List<TransferEntity> transferEntitiesOutput;


    @OneToMany(mappedBy="inventory")
    private List<InventoryDetailEntity> inventoryDetailEntities;

    public InventoriesEntity(int id, String code, String name, String address, String phone, String mail, Timestamp createAt, Timestamp updateAt, Timestamp deletedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.mail = mail;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.deletedAt = deletedAt;
    }
}
