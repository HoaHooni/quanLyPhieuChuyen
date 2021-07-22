package com.sapo.edu.managetransferslip.model.dto;

import com.sapo.edu.managetransferslip.model.entity.RolesEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RolesDTO {

    int id;

    String name;

    String description;


    public RolesDTO(RolesEntity rolesEntity) {
        this.id = rolesEntity.getId();
        this.name = rolesEntity.getName();
        this.description = rolesEntity.getDescription();
    }


}
