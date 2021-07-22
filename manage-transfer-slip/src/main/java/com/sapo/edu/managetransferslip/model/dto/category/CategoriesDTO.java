package com.sapo.edu.managetransferslip.model.dto.category;

import com.sapo.edu.managetransferslip.model.entity.CategoriesEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriesDTO {

    private int id;

    private String code;

    private String name;

    private String description;

    public CategoriesDTO(CategoriesEntity categoriesEntity) {
        this.id = categoriesEntity.getId();
        this.code = categoriesEntity.getCode();
        this.name = categoriesEntity.getName();
        this.description = categoriesEntity.getDescription();
    }
}

