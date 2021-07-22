package com.sapo.edu.managetransferslip.repository;


import com.sapo.edu.managetransferslip.model.entity.InventoriesEntity;
import com.sapo.edu.managetransferslip.model.entity.InventoryDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryDetailRepository extends JpaRepository<InventoryDetailEntity, Integer> {


    //get element by product_id and inventory__id
    @Query(value = "Select * from inventory_detail where product_id = :pr and inventory_id = :inventory", nativeQuery = true)
    InventoryDetailEntity findWithElementId(@Param("pr") int product, @Param("inventory") int inventory);

    //get list product in inventory
    @Query(value = "Select * from inventory_detail where inventory_id = :inventory", nativeQuery = true)
    List<InventoryDetailEntity> findAllProductsInInventory(@Param("inventory") int inVen);


    @Query(value = "Select * from inventory_detail where product_id = :pr", nativeQuery = true)
    InventoryDetailEntity findByProductId(@Param("pr") int productId);

    @Query(value = "Select * from inventory_detail where product_id = :pr", nativeQuery = true)
    List<InventoryDetailEntity> findByListProductId(@Param("pr") int productId);

    @Query(value = "Select * from inventory_detail where inventory_id = :inven order by create_at desc limit :offset,:limit",nativeQuery = true)
    List<InventoryDetailEntity> findByInventoryId(@Param("inven") int inventoryId,@Param("offset") int offset,@Param("limit") int limit);

    @Query(value = "delete from inventory_detail where product_id = :product ", nativeQuery = true)
    @Transactional
    void deleteByProductId(@Param("product") int id);

    @Query(value = "delete from inventory_detail where inventory_id = :inventory", nativeQuery = true)
    @Transactional
    void deleteByInventoryId(@Param("inventory") int inventoryId);

    @Query(value = "select sum(number_pro) from inventory_detail", nativeQuery = true)
    long sum();

    @Query(value = "select count(product_id) from inventory_detail", nativeQuery = true)
    long count();

    @Query(value = "select sum(number_pro) from inventory_detail where inventory_id = :id", nativeQuery = true)
    int sumProductOfInventory(@Param("id") int id);

    @Query(value = "select count(number_pro) from inventory_detail where inventory_id = :id", nativeQuery = true)
    long countProductOfInventory(@Param("id") int id);

    @Query(value = "select sum(number_pro) from inventory_detail where product_id = :id", nativeQuery = true)
    int sumProductById(@Param("id") int id);

    @Query(value = "select * from inventory_detail where inventory_id = :id order by number_pro asc limit :offset,:limit",nativeQuery = true)
    List<InventoryDetailEntity> pagination(@Param("id") int id,@Param("offset") int offset, @Param("limit") int limit);

    @Query(value = "select count(*)  from inventory_detail",nativeQuery = true)
    long countAll();

    @Query(value = "select * from inventory_detail where inventory_id = :id order by number_pro desc limit :offset,:limit",nativeQuery = true)
    List<InventoryDetailEntity> paginationProduct(@Param("id") int id,@Param("offset") int offset,@Param("limit") int limit);

    @Override
    Optional<InventoryDetailEntity> findById(Integer integer);

    <S extends InventoryDetailEntity> List<S> saveAll(Iterable<S> iterable);

    @Override
    @Transactional
    <S extends InventoryDetailEntity> S save(S s);


    @Override
    List<InventoryDetailEntity> findAll();


}

