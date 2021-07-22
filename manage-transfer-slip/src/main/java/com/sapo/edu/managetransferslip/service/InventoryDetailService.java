package com.sapo.edu.managetransferslip.service;





import com.sapo.edu.managetransferslip.model.dto.InventoryDetailDTO;
import com.sapo.edu.managetransferslip.model.dto.InventoryDetailReportDTO;
import com.sapo.edu.managetransferslip.model.dto.product.ProductsDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InventoryDetailService {


    ResponseEntity<InventoryDetailDTO> add(InventoryDetailDTO inventoryDetailDTO);

    ResponseEntity<InventoryDetailDTO> deleteByProductId(int id);

    ResponseEntity<InventoryDetailDTO> deleteByInventoryId(int id);

    ResponseEntity<InventoryDetailDTO> update(int id,int numberProduct);

    List<InventoryDetailDTO> getData();

    boolean already(InventoryDetailDTO inventoryDetailDTO);

    //new code
    long countProduct();

    long sumNumberProduct();

    long countByInventoryId(int id);

    long sumByInventoryId(int id);

    long sumProductById(int id);

    long sizeProduct(int id);

    InventoryDetailReportDTO detailsById(int id,int page,int limit);

    InventoryDetailReportDTO findWithKey(int id,String key,int page,int limit);

    long sizeFindByKey(int id,String key);
}
