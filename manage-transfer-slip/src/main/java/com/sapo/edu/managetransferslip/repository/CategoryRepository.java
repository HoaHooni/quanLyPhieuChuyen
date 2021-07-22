package com.sapo.edu.managetransferslip.repository;

import com.sapo.edu.managetransferslip.model.entity.CategoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoriesEntity,Integer> {


    CategoriesEntity findByName(String name);
}
