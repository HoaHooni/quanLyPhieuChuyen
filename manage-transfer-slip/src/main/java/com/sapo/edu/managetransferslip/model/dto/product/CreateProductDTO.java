package com.sapo.edu.managetransferslip.model.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class CreateProductDTO {
    private int id;

    @NotBlank(message = "Name is mandatory")
    private String code;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String description;

    @NotBlank(message = "Name is mandatory")
    private String size;

    @NotBlank(message = "Name is mandatory")
    private String color;

    @Min(0)
    private Double price;

    private String link;

    //    @NotBlank(message = "Name is mandatory")
    @NotNull(message = "Name cannot be null")
    private int category_id;



    @Override
    public String toString() {
        return "InputProductDTO{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", size='" + size + '\'' +
                ", color='" + color + '\'' +
                ", price=" + price +
                ", link='" + link + '\'' +
                ", category_id=" + category_id +
                '}';
    }
}
