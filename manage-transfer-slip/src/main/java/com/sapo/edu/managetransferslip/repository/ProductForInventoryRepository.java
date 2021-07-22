package com.sapo.edu.managetransferslip.repository;

import com.sapo.edu.managetransferslip.model.entity.InventoryDetailEntity;
import com.sapo.edu.managetransferslip.model.entity.ProductsEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductForInventoryRepository extends JpaRepository<InventoryDetailEntity, Integer> {
    @Query(value = "SELECT * FROM inventory_detail i LEFT JOIN products p ON i.product_id = p.id where i.inventory_id= ?1 and _deleted_at is null group by inventory_id,product_id order by p.id desc", nativeQuery = true)
    List<InventoryDetailEntity> findProductWithoutDelete(int inventoryId, Pageable pageable);

    @Query(value = "SELECT * FROM inventory_detail i LEFT JOIN products p ON i.product_id = p.id where i.inventory_id= ?1 and _deleted_at is null group by inventory_id,product_id order by p.id desc", nativeQuery = true)
    List<InventoryDetailEntity> findProductWithoutDelete0(int inventoryId);

//    @Query(value = "select * from products", nativeQuery = true)
    @Query(value = "SELECT * FROM inventory_detail i LEFT JOIN products p ON i.product_id = p.id where i.inventory_id= :id and _deleted_at is null and (p.name like %:keySearch% or p.code like  %:keySearch% ) group by inventory_id,product_id order by p.id desc", nativeQuery = true)
    List<InventoryDetailEntity> searchProductByInventory( @Param("id")int id,@Param("keySearch")String keySearch,Pageable pageable);

    @Query(value = "SELECT * FROM inventory_detail i LEFT JOIN products p ON i.product_id = p.id where i.inventory_id= :id and _deleted_at is null and (p.name like %:keySearch% or p.code like  %:keySearch% ) group by inventory_id,product_id order by p.id desc", nativeQuery = true)
    List<InventoryDetailEntity> searchProductByInventory( @Param("id")int id,@Param("keySearch")String keySearch);

    @Query(value = "SELECT count(*) FROM inventory_detail i LEFT JOIN products p ON i.product_id = p.id where i.inventory_id= :id and _deleted_at is null",nativeQuery = true)
    int getTotalRowInventoryDetail(@Param("id")int id);

    @Query(value = "SELECT count(*) FROM inventory_detail i LEFT JOIN products p ON i.product_id = p.id where i.inventory_id= :id and _deleted_at is null and (p.name like %:keySearch% or p.code like  %:keySearch% )",nativeQuery = true)
    int getTotalRowSearchInventoryDetail(@Param("id")int id,@Param("keySearch")String keySearch);

    @Query(value = "SELECT * FROM products p left JOIN inventory_detail i on product_id= p.id where inventory_id = :inventory and p.id= :product",nativeQuery = true)
    InventoryDetailEntity ProductForInventoryDTO(@Param("inventory")int id,@Param("product")int idPro);

    @Query(value = "SELECT id FROM inventory_detail  order by id desc limit 1", nativeQuery = true)
    int getLastInventoryDetail();

    @Query(value = "SELECT count(*) FROM sapo.inventory_detail where inventory_id=?1", nativeQuery = true)
    int getTotalProductInInventory(int inventoryId);
}