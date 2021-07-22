package com.sapo.edu.managetransferslip.controller;

import com.sapo.edu.managetransferslip.model.dto.category.CategoriesDTO;
import com.sapo.edu.managetransferslip.model.dto.category.CreateCategoryDTO;
import com.sapo.edu.managetransferslip.model.dto.category.UpdateCategoryDTO;
import com.sapo.edu.managetransferslip.model.dto.product.CreateProductDTO;
import com.sapo.edu.managetransferslip.model.dto.product.UpdateProductDTO;
import com.sapo.edu.managetransferslip.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3006")
@RequestMapping("/admin/category")
public class CategoryController {

    private final CategoryService CATEGORY_SERVICE;

    public CategoryController(CategoryService category_service) {
        CATEGORY_SERVICE = category_service;
    }

    @GetMapping(value = "")
    public List<CategoriesDTO> getAll(){
        return CATEGORY_SERVICE.getAll();
    }

//    @GetMapping(value = "/{id}")
//    public List<CategoriesDTO> getById(@PathVariable int id){
//        return CATEGORY_SERVICE.getAll();
//    }
    /***
     * Block code CRUD
     ***/


    @PostMapping(value = "")
    public ResponseEntity create(@RequestBody @Valid CreateCategoryDTO createCategoryDTO) {
        CATEGORY_SERVICE.create(createCategoryDTO);
        System.out.println(createCategoryDTO);
        return new ResponseEntity(HttpStatus.MULTI_STATUS);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity update(@RequestBody @Valid UpdateCategoryDTO updateCategoryDTO, @PathVariable int id) {
        CATEGORY_SERVICE.update(updateCategoryDTO, id);
        return new ResponseEntity(HttpStatus.MULTI_STATUS);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable int id) {
        CATEGORY_SERVICE.delete(id);
        return new ResponseEntity(HttpStatus.MULTI_STATUS);
    }

    @DeleteMapping(value = "/undo/{id}")
    public ResponseEntity undo(@PathVariable int id) {
        CATEGORY_SERVICE.undo(id);
        return new ResponseEntity(HttpStatus.MULTI_STATUS);
    }

}
