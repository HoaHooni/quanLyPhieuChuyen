package com.sapo.edu.managetransferslip.repository;

import com.sapo.edu.managetransferslip.model.entity.ProductsEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductsEntity, Integer> {

    @Query(value = "select * from products where deleted_at is null order by id desc", nativeQuery = true)
    List<ProductsEntity> findProductWithoutDelete(Pageable pageable);

    @Query(value = "select * from products p where deleted_at is null and (p.name like %:keySearch% or p.description like  %:keySearch%  or p.code like  %:keySearch% ) order by id desc", nativeQuery = true)
    List<ProductsEntity> searchProductByKey(@Param("keySearch") String keySearch, Pageable pageable);

    @Query(value = "select * from products p where deleted_at is null and p.size = ?1", nativeQuery = true)
    List<ProductsEntity> filterBySize(String keyFilter, Pageable pageable);

    @Query(value = "select * from products where deleted_at is null and price >:minPrice and price < :maxPrice", nativeQuery = true)
    List<ProductsEntity> filterByPrice(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, Pageable pageable);

    @Query(value = "select * from products p where deleted_at is null and p.category_id = ?1", nativeQuery = true)
    List<ProductsEntity> filterByCategory(String keyFilter, Pageable pageable);

    @Query(value = "select * from products p where deleted_at is null and p.color Like %:keyFilter%", nativeQuery = true)
    List<ProductsEntity> filterByColor(@Param("keyFilter") String keyFilter, Pageable pageable);

    @Query(value = "select * from products where deleted_at is null order by name asc", nativeQuery = true)
    List<ProductsEntity> sortAscByName(Pageable pageable);

    @Query(value = "select * from products where deleted_at is null order by name desc", nativeQuery = true)
    List<ProductsEntity> sortDescByName(Pageable pageable);

    @Query(value = "select * from products where deleted_at is null order by price asc", nativeQuery = true)
    List<ProductsEntity> sortAscByPrice(Pageable pageable);

    @Query(value = "select * from products where deleted_at is null order by price desc", nativeQuery = true)
    List<ProductsEntity> sortDescByPrice(Pageable pageable);

    @Query(value = "select * from products where deleted_at is null order by price asc", nativeQuery = true)
    List<ProductsEntity> sortAscByNumber(Pageable pageable);

    @Query(value = "select * from products where deleted_at is null  order by price desc", nativeQuery = true)
    List<ProductsEntity> sortDescByNumber(Pageable pageable);

    ProductsEntity findByCode(String code);

    @Query(value = "select * from products where name like concat('%',:key,'%') or code like concat('%',:key,'%') " +
            "order by create_at " +
            "desc limit :offset,:limit", nativeQuery = true)
    List<ProductsEntity> findByKey(@Param("key") String key, @Param("offset") int offset, @Param("limit") int limit);

    @Query(value = "SELECT * FROM products  order by id desc  limit 1;", nativeQuery = true)
    ProductsEntity getLastProductEntity();


    @Query(value = "select * from products where name like concat('%',:key,'%') or code like concat('%',:key,'%') ", nativeQuery = true)
    List<ProductsEntity> sizeFindByKey(@Param("key") String key);

    boolean existsByCode(String code);

    @Query(value = "SELECT id FROM products  order by id desc  limit 1", nativeQuery = true)
    int getLastProduct();
}
