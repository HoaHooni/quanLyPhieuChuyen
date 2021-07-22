package com.sapo.edu.managetransferslip.service.impl;

import com.sapo.edu.managetransferslip.model.dto.category.CategoriesDTO;
import com.sapo.edu.managetransferslip.model.dto.category.CreateCategoryDTO;
import com.sapo.edu.managetransferslip.model.dto.category.UpdateCategoryDTO;
import com.sapo.edu.managetransferslip.repository.CategoryRepository;
import com.sapo.edu.managetransferslip.service.CategoryService;
import com.sapo.edu.managetransferslip.model.entity.CategoriesEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService<CategoriesDTO> {

    private final CategoryRepository CATEGORY_REPOSITORY;
    private Timestamp currentTime = new Timestamp(System.currentTimeMillis());
    public CategoryServiceImpl(CategoryRepository category_repository) {
        CATEGORY_REPOSITORY = category_repository;
    }


    @Override
    public List<CategoriesDTO> getAll() {
        List<CategoriesDTO> categoriesDTOS = new LinkedList<>();
        List<CategoriesEntity> categoriesEntities = CATEGORY_REPOSITORY.findAll();
        for(CategoriesEntity categoriesEntity:categoriesEntities){
            categoriesDTOS.add(new CategoriesDTO(categoriesEntity));
        }
        return categoriesDTOS;
    }

    @Override
    public CategoriesDTO create(CreateCategoryDTO createCategoryDTO) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        CategoriesEntity categoriesEntity = modelMapper.map(createCategoryDTO,CategoriesEntity.class);
        CATEGORY_REPOSITORY.save(categoriesEntity);
        return new CategoriesDTO(categoriesEntity);
    }

    @Override
    public CategoriesDTO update(UpdateCategoryDTO updateCategoryDTO, int id) {
        CategoriesEntity categoriesEntity = CATEGORY_REPOSITORY.getById(id);
        if(updateCategoryDTO.getCode()!=null){
            categoriesEntity.setCode(updateCategoryDTO.getCode());
        }
        if(updateCategoryDTO.getName()!=null){
            categoriesEntity.setName(updateCategoryDTO.getName());
        }
        if(updateCategoryDTO.getDescription()!=null){
            categoriesEntity.setDescription(updateCategoryDTO.getDescription());
        }

        return null;
    }

    @Override
    public void delete(int id) {
        CategoriesEntity categoriesEntity = CATEGORY_REPOSITORY.getById(id);
        categoriesEntity.setDeletedAt(currentTime);
        CATEGORY_REPOSITORY.save(categoriesEntity);

    }

    @Override
    public void undo(int id) {
        CategoriesEntity categoriesEntity = CATEGORY_REPOSITORY.getById(id);
        categoriesEntity.setDeletedAt(null);
        CATEGORY_REPOSITORY.save(categoriesEntity);
    }
}