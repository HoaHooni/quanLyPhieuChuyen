package com.sapo.edu.managetransferslip.repository;

import com.sapo.edu.managetransferslip.model.entity.TransferDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferDetailRepository extends JpaRepository<TransferDetailEntity, Integer> {
}
