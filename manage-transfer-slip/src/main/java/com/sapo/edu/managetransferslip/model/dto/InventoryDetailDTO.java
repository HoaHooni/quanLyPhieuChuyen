package com.sapo.edu.managetransferslip.model.dto;

import com.sapo.edu.managetransferslip.model.dto.product.ProductsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InventoryDetailDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int product_id;

    private int inventory_id;

    private int numberPro;

    @java.lang.Override
    public java.lang.String toString() {
        return "InventoryDetailEntity{" +
                "id=" + id +
                ", product=" + product_id +
                ", inventory_id=" + inventory_id +
                ", numberPro=" + numberPro +
                '}';
    }
}
