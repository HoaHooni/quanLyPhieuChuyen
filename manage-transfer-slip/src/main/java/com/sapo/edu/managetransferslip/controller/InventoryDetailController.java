package com.sapo.edu.managetransferslip.controller;


import com.sapo.edu.managetransferslip.model.dto.InventoriesDTO;
import com.sapo.edu.managetransferslip.model.dto.InventoryDetailDTO;
import com.sapo.edu.managetransferslip.model.dto.InventoryDetailReportDTO;
import com.sapo.edu.managetransferslip.model.dto.product.ProductsDTO;
import com.sapo.edu.managetransferslip.service.InventoryDetailService;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("hasRole('ROLE_MANAGER')")
@RequestMapping("/admin/inventory-details")

public class InventoryDetailController {

    private final InventoryDetailService inventoryDetailService;

    public InventoryDetailController(InventoryDetailService inventoryDetailService) {
        this.inventoryDetailService = inventoryDetailService;
    }

    //done
    @GetMapping()
    public List<InventoryDetailDTO> getData(){
        return inventoryDetailService.getData();
    }


    //done
    @PostMapping()
    public ResponseEntity<InventoryDetailDTO> addData(@RequestBody InventoryDetailDTO inventoryDetailDTO){
        return inventoryDetailService.add(inventoryDetailDTO);
    }

    @DeleteMapping("/inventory")
    public ResponseEntity<InventoryDetailDTO> deleteByInventory(@RequestParam int inventory){
        return inventoryDetailService.deleteByInventoryId(inventory);
    }

    @DeleteMapping("/product")
    public ResponseEntity<InventoryDetailDTO> deleteByProduct(@RequestParam int product){
        return inventoryDetailService.deleteByProductId(product);
    }


    /*
        return list inventorydetails DTO is data of product in inventory have id = :id

     */

    /*
        count  product in inventory
     */
    @GetMapping("/count")
    public long getCount(){
        return inventoryDetailService.countProduct();
    }

    /*
        sum of product in all inventory
     */
    @GetMapping("/sum")
    public long getSum(){
        return inventoryDetailService.sumNumberProduct();
    }


    /*
    *
    * return a long result is sum of number product in inventory
    * */
    @GetMapping("/sum/{id}")
    public long getSumByInventoryId(@PathVariable("id") int id){
        return inventoryDetailService.sumByInventoryId(id);
    }


    /*
    * return a long result is count of inventory have id
    * Parameter: id is id of inventory
    * */
    @GetMapping("/count/{id}")
    public long getCountByInventoryId(@PathVariable("id") int id){
        return inventoryDetailService.countByInventoryId(id);
    }



    @GetMapping("/product/{id}")
    public long getSumProductId(@PathVariable("id") int id){
       return inventoryDetailService.sumProductById(id);
    }


    @GetMapping("/page-products/size")
    public long sizeProduct(@RequestParam int id)
    {
        return inventoryDetailService.sizeProduct(id);
    }

    @GetMapping("/page")
    public  InventoryDetailReportDTO dataDetails(@RequestParam int id,@RequestParam int page,@RequestParam int limit){
        return inventoryDetailService.detailsById(id,page,limit);
    }

    @GetMapping("/find-by-key")
    public InventoryDetailReportDTO findByKey(@RequestParam int id,@RequestParam String key, @RequestParam int page,@RequestParam int limit){
        return inventoryDetailService.findWithKey(id,key,page,limit);
    }

    @GetMapping("/find-by-key-size")
    public  long sizeFindByKey(@RequestParam int id,@RequestParam String key){
        return inventoryDetailService.sizeFindByKey(id,key);
    }
}
