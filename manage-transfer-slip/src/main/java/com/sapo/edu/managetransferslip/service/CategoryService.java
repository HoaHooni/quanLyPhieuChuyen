package com.sapo.edu.managetransferslip.service;

import com.sapo.edu.managetransferslip.model.dto.category.CategoriesDTO;
import com.sapo.edu.managetransferslip.model.dto.category.CreateCategoryDTO;
import com.sapo.edu.managetransferslip.model.dto.category.UpdateCategoryDTO;

import java.util.List;

public interface CategoryService<T> {

    List<T> getAll();

    T create(CreateCategoryDTO createCategoryDTO);

    T update(UpdateCategoryDTO createCategoryDTO, int id);

    void delete(int id);

    void undo(int id);


}
