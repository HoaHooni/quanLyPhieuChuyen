package com.sapo.edu.managetransferslip.model.dto.category;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
public class CreateCategoryDTO {
    private int id;

    @NotBlank(message = "Name is mandatory")
    private String code;

    private String name;

    private String description;
}

