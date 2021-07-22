package com.sapo.edu.managetransferslip.repository;

import com.sapo.edu.managetransferslip.model.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RolesEntity, Integer> {
    Optional<RolesEntity> findByName(String name);
}
