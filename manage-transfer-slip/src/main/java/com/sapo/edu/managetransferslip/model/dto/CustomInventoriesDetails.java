package com.sapo.edu.managetransferslip.model.dto;

import com.sapo.edu.managetransferslip.model.dto.product.ProductsDTO;
import com.sapo.edu.managetransferslip.model.entity.ProductsEntity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CustomInventoriesDetails {
    private ProductsDTO productsDTO;
    private int numberProduct;
    private int idDetails;
    public CustomInventoriesDetails(ProductsDTO productsDTO, int numberProduct,int idDetails) {
        this.productsDTO = productsDTO;
        this.numberProduct = numberProduct;
        this.idDetails = idDetails;
    }

    @Override
    public String toString() {
        return "CustomInventoriesDetails{" +
                "productsDTO=" + productsDTO +
                ", numberProduct=" + numberProduct +
                ", idDetails=" + idDetails +
                '}';
    }
}
