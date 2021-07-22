package com.sapo.edu.managetransferslip.controller;


import com.sapo.edu.managetransferslip.model.dto.product.ProductForInventoryDTO;
import com.sapo.edu.managetransferslip.model.dto.product.ProductsDTO;
import com.sapo.edu.managetransferslip.model.dto.product.UpdateProductDTO;
import com.sapo.edu.managetransferslip.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Stack;

@RestController
@CrossOrigin
@RequestMapping("/admin/products")
public class ProductController {

    private final ProductService PRODUCT_SERVICE;

    public ProductController(ProductService product_service) {
        PRODUCT_SERVICE = product_service;
    }

    /***
     * Block code CRUD
     ***/

    @GetMapping(value = "")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public List<ProductForInventoryDTO> get(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return PRODUCT_SERVICE.getAll(pageable);
    }

    @GetMapping(value = "/list/{inventoryId}")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public List<ProductForInventoryDTO> getAll(
            @PathVariable int inventoryId,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false) Integer size
    ) {
        return PRODUCT_SERVICE.getProductForInventory(inventoryId, page - 1, size);
    }


    @PostMapping(value = "")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER')")
    public ResponseEntity create(@RequestBody @Valid ProductForInventoryDTO productForInventoryDTO) {
        List<String> messages = PRODUCT_SERVICE.createInventoryDetail(productForInventoryDTO);
        return new ResponseEntity(messages, HttpStatus.MULTI_STATUS);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER')")
    public ResponseEntity update(@RequestBody @Valid UpdateProductDTO updateProductDTO, @PathVariable int id) {
        PRODUCT_SERVICE.update(updateProductDTO, id);
        return new ResponseEntity(HttpStatus.MULTI_STATUS);
    }

    @PutMapping(value = "/inventory/{id}")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER')")
    public ResponseEntity updateProductForInventory(@RequestBody @Valid ProductForInventoryDTO productForInventoryDTO, @PathVariable int id) {
        String message = PRODUCT_SERVICE.updateProductForInventory(productForInventoryDTO, id);
        return new ResponseEntity(message, HttpStatus.MULTI_STATUS);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER')")
    public ResponseEntity delete(@PathVariable int id) {
        PRODUCT_SERVICE.delete(id);
        return new ResponseEntity(HttpStatus.MULTI_STATUS);
    }

    @DeleteMapping(value = "/undo/{id}")
    public ResponseEntity undo(@PathVariable int id) {
        PRODUCT_SERVICE.undo(id);
        return new ResponseEntity(HttpStatus.MULTI_STATUS);
    }



    @GetMapping(value = "/filter-by-size/{keyFilter}")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public List<ProductsDTO> filterBySize(
            @PathVariable String keyFilter,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return PRODUCT_SERVICE.filterBySize(keyFilter, pageable);
    }

    @GetMapping(value = "/filter-by-price")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public List<ProductsDTO> filterByPrice(
            @RequestParam(name = "min-price", required = false, defaultValue = "0") Double minPrice,
            @RequestParam(name = "max-price", required = false, defaultValue = "5") Double maxPrice,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return PRODUCT_SERVICE.filterByPrice(minPrice, maxPrice, pageable);
    }

    @GetMapping(value = "/filter-by-category/{keyFilter}")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public List<ProductsDTO> filterByCategory(
            @PathVariable String keyFilter,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return PRODUCT_SERVICE.filterByCategory(keyFilter, pageable);
    }

    @GetMapping(value = "/filter-by-color/{keyFilter}")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public List<ProductsDTO> filterByColor(
            @PathVariable String keyFilter,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return PRODUCT_SERVICE.filterByColor(keyFilter, pageable);
    }

    /***
     * Block code sort product by name, price, number
     ***/

    @GetMapping(value = "/sort-asc-by-name")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public List<ProductsDTO> sortAscByName(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return PRODUCT_SERVICE.sortAscByName(pageable);
    }

    @GetMapping(value = "/sort-desc-by-name")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public List<ProductsDTO> sortDescByName(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return PRODUCT_SERVICE.sortDescByName(pageable);
    }

    @GetMapping(value = "/sort-asc-by-price")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public List<ProductsDTO> sortAscByPrice(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return PRODUCT_SERVICE.sortAscByPrice(pageable);
    }

    @GetMapping(value = "/sort-desc-by-price")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public List<ProductsDTO> sortDescByPrice(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return PRODUCT_SERVICE.sortDescByPrice(pageable);
    }

    @GetMapping(value = "/sort-asc-by-number")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public List<ProductsDTO> sortAscByNumber(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return PRODUCT_SERVICE.sortAscByNumber(pageable);
    }

    @GetMapping(value = "/sort-desc-by-number")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public List<ProductsDTO> sortDescByNumber(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return PRODUCT_SERVICE.sortAscByNumber(pageable);
    }

    @GetMapping(value = "/{inventory}")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public ResponseEntity<?> showData(
            @PathVariable int inventory,
            @RequestParam(name = "key-search", required = false) String keySearch,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false) Integer size
    ) {
        List data = PRODUCT_SERVICE.showData(keySearch, inventory, page-1, size);

        return new ResponseEntity(data, HttpStatus.MULTI_STATUS);
    }
    @GetMapping(value = "/{inventory}/{id}")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public ResponseEntity<?> getProductForInventoryById(
            @PathVariable int inventory,
            @PathVariable int id
    ){
        ProductForInventoryDTO data = PRODUCT_SERVICE.getProductForInventoryById(inventory,id);
        return new ResponseEntity(data, HttpStatus.MULTI_STATUS);
    }

    @GetMapping(value = "/last-product")
    public ResponseEntity<?> getLastProduct(){
        int data = PRODUCT_SERVICE.getLastProduct();
        return new ResponseEntity(data, HttpStatus.MULTI_STATUS);
    }
    @GetMapping(value = "/last-inventory-detail")
    public ResponseEntity<?> getLastInventoryDetail(){
        int data = PRODUCT_SERVICE.getLastInventoryDetail();
        return new ResponseEntity(data, HttpStatus.MULTI_STATUS);
    }

    @GetMapping(value = "/total-product-in-inventory/{inventoryId}")
    public ResponseEntity<Integer> getTotalProductInInventory(@PathVariable int inventoryId){
        int data = PRODUCT_SERVICE.getTotalProductInInventory(inventoryId);
        return new ResponseEntity<>(data,HttpStatus.MULTI_STATUS);
    }
}
