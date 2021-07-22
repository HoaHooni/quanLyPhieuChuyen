package com.sapo.edu.managetransferslip.model.dto.product;

import com.sapo.edu.managetransferslip.model.entity.InventoriesEntity;
import com.sapo.edu.managetransferslip.model.entity.InventoryDetailEntity;
import com.sapo.edu.managetransferslip.model.entity.ProductsEntity;
import lombok.Getter;
import lombok.Setter;

//import java.util.Date;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
public class ProductForInventoryDTO {
    private int id;

    private int productId;

    private String code;

    private String name;

    private Double price;

    private String categoryName;

    private String description;

    private String size;

    private String color;

    private int inventoryId;

    private int numberPro;

    private String link;

    private Timestamp createAt;

    public ProductForInventoryDTO() {
    }

    public ProductForInventoryDTO(InventoryDetailEntity inventoryDetailEntity) {
        this.id = inventoryDetailEntity.getId();
        this.productId = inventoryDetailEntity.getProduct().getId();
        this.code = inventoryDetailEntity.getProduct().getCode();
        this.name = inventoryDetailEntity.getProduct().getName();
        this.price = inventoryDetailEntity.getProduct().getPrice();
        this.categoryName = inventoryDetailEntity.getProduct().getCategory().getName();
        this.description = inventoryDetailEntity.getProduct().getDescription();
        this.size = inventoryDetailEntity.getProduct().getSize();
        this.color = inventoryDetailEntity.getProduct().getColor();
        this.inventoryId = inventoryDetailEntity.getInventory().getId();
        this.numberPro = inventoryDetailEntity.getNumberPro();
        this.link = inventoryDetailEntity.getProduct().getLink();
        this.createAt = inventoryDetailEntity.getProduct().getCreateAt();

    }
}
