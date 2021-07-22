package com.sapo.edu.managetransferslip.repository;

import com.sapo.edu.managetransferslip.model.entity.InventoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface InventoriesRepository extends JpaRepository<InventoriesEntity,Integer> {
}
