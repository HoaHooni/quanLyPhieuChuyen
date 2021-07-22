package com.sapo.edu.managetransferslip.model.dto.product;

import com.sapo.edu.managetransferslip.model.entity.InventoryDetailEntity;
import com.sapo.edu.managetransferslip.model.entity.ProductsEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProductsDTO {

    private int id;

    private String code;

    private String name;

    private String description;

    private String size;

    private String color;

    private Double price;

    private String link;

    private int number;

    private String categoryName;

    public ProductsDTO() {
    }
    
    public ProductsDTO(ProductsEntity product) {
        this.id = product.getId();
        this.code = product.getCode();
        this.name = product.getName();
        this.description = product.getDescription();
        this.size = product.getSize();
        this.color = product.getColor();
        this.price = product.getPrice();
        this.link = product.getLink();
        this.categoryName = product.getCategory().getName();
    }

}
