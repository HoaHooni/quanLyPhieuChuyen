package com.sapo.edu.managetransferslip.model.dto.product;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class UpdateProductDTO {
    private int id;

    private String code;

    private String name;

    private String description;

    private String size;

    private String color;

    private Double price;

    private String link;

    private int category_id;

}