package com.sapo.edu.managetransferslip.model.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sapo.edu.managetransferslip.model.entity.RolesEntity;
import com.sapo.edu.managetransferslip.model.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.*;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsersDTO {

    private int id;

    private String username;
    private String code;
    private String email;

    private String phone;

    private String address;


    private Date dob;

    private int status;

    private Timestamp createAt;

    private Timestamp updateAt;
    
    private Timestamp deleteAt;

    Set<String> roles;
    Set<String>  role;

    private int inventoryId;

    public UsersDTO(UsersEntity usersEntity) {
        this.id = usersEntity.getId();
        this.username = usersEntity.getUsername();
        this.code = usersEntity.getCode();
        this.email = usersEntity.getEmail();
        this.phone = usersEntity.getPhone();
        this.address = usersEntity.getAddress();
        this.dob = usersEntity.getDob();
        this.status = usersEntity.getStatus();
        this.createAt = usersEntity.getCreateAt();
        this.updateAt = usersEntity.getUpdateAt();
        this.deleteAt = usersEntity.getDeleteAt();
        Set<String> roles = new HashSet<>();
        for(RolesEntity rolesEntity: usersEntity.getRoles()){
            //RolesDTO rolesDTO = new RolesDTO(rolesEntity);
            String role = rolesEntity.getName();
            roles.add(role);
        }
        this.roles =  roles;
        Set<String> roleDes = new HashSet<>();
        for(RolesEntity rolesEntity: usersEntity.getRoles()){

            String role = rolesEntity.getDescription();
            roleDes.add(role);
        }
        this.role = roleDes;
        this.inventoryId = usersEntity.getInventoryId();
    }



}


