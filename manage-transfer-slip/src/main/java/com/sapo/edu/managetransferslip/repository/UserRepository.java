package com.sapo.edu.managetransferslip.repository;

import com.sapo.edu.managetransferslip.model.entity.UsersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, Integer> {
    UsersEntity findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByCode(String code);

    List<UsersEntity> findAllByDeleteAtIsNullOrderByIdDesc();
    Page<UsersEntity> findAllByDeleteAtIsNullOrderByIdDesc(Pageable pageable);

    UsersEntity findByIdAndDeleteAtIsNull(Integer id);

    @Query(value = "select * from users u where u.delete_at is null and (u.username like %:keySearch% or u.email like  %:keySearch%  or u.code like  %:keySearch% or u.phone like  %:keySearch% or u.address like  %:keySearch% ) order by u.id desc", nativeQuery = true)
    List<UsersEntity> searchUserByKey(@Param("keySearch") String keySearch);

    @Query(value = "select * from users u where u.delete_at is null and (u.username like %:keySearch% or u.email like  %:keySearch%  or u.code like  %:keySearch% or u.phone like  %:keySearch% or u.address like  %:keySearch% ) order by u.id desc " +
            " limit :page, :limit", nativeQuery = true)
    List<UsersEntity> searchUserByKey(@Param("keySearch") String keySearch,@Param("page") int page,@Param("limit") int limit);

    UsersEntity findFirstByOrderByIdDesc();
}
