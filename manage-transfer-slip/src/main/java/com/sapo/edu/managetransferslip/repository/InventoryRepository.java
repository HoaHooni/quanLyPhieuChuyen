package com.sapo.edu.managetransferslip.repository;

import com.sapo.edu.managetransferslip.model.entity.InventoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface InventoryRepository extends JpaRepository<InventoriesEntity,Integer> {

    @Query(value = "Select * from inventories where deleted_at is null",nativeQuery = true)
    List<InventoriesEntity> getAll();

    @Query(value = "select * from inventories where code = :code and deleted_at is null",nativeQuery = true)
    InventoriesEntity findByCode(@Param("code") String code);

    @Query(value = "select * from inventories where code = :code where deleted_at is not null",nativeQuery = true)
    InventoriesEntity findWasDeleted(@Param("code") String code);

    @Override
    Optional<InventoriesEntity> findById(Integer integer);

    @Query(value = "Select * from inventories where deleted_at is null and (code like concat('%',:key,'%') or name like concat('%',:key,'%') or address like concat('%',:key,'%') or phone like concat('%',:key,'%') or mail like concat('%',:key,'%') " +
            "or create_at like concat('%',:key,'%') or" +
            " update_at like concat('%',:key,'%')) order by create_at desc limit :offset,:limit",nativeQuery = true)
    List<InventoriesEntity> findByKey(@Param("key") String key,@Param("offset") int offset,@Param("limit") int limit);

    @Query(value ="Select count(*) from inventories where deleted_at is null and code like concat('%',:key,'%') or name like concat('%',:key,'%') or address like concat('%',:key,'%') or phone like concat('%',:key,'%') or mail like concat('%',:key,'%') " +
            "or create_at like concat('%',:key,'%') or" +
            " update_at like concat('%',:key,'%')",nativeQuery = true)
    long countFinding(String key);


    @Query(value = "Select * from inventories where deleted_at is null and (code = :key or name = :key or address = :key or phone = :key or mail = :key )",nativeQuery = true)
    List<InventoriesEntity> findExact(@Param("key") String key);

    @Query(value = "SELECT * FROM inventories where " +
            "year(update_at) = :year " +
            "and month(update_at) = :month " +
            "and day(update_at) = :day " +
            "and deleted_at is null " +
            "and code like concat('%',:key,'%') " +
            "or name like concat('%',:key,'%') " +
            "or address like concat('%',:key,'%') or " +
            "phone like concat('%',:key,'%') or " +
            "mail like concat('%',:key,'%')",nativeQuery = true)
    List<InventoriesEntity> findFilter(@Param("key") String key,@Param("year") String year,@Param("month") String month,@Param("day") String day);

    @Query(value = "SELECT * FROM inventories where " +
            "year(update_at) = :year " +
            "and month(update_at) = :month " +
            "and deleted_at is null " +
            "and code like concat('%',:key,'%') " +
            "or name like concat('%',:key,'%') " +
            "or address like concat('%',:key,'%') or " +
            "phone like concat('%',:key,'%') or " +
            "mail like concat('%',:key,'%')",nativeQuery = true)
    List<InventoriesEntity> findWithoutDay(@Param("key") String key,@Param("year") String year,@Param("month") String month);

    @Query(value = "SELECT * FROM inventories where " +
            "year(update_at) = :year " +
            "and day(update_at) = :day " +
            "and deleted_at is null " +
            "and code like concat('%',:key,'%') " +
            "or name like concat('%',:key,'%') " +
            "or address like concat('%',:key,'%') or " +
            "phone like concat('%',:key,'%') or " +
            "mail like concat('%',:key,'%')",nativeQuery = true)
    List<InventoriesEntity> findWithoutMonth(@Param("key") String key,@Param("year") String year,@Param("day") String day);

    @Query(value = "SELECT * FROM inventories where " +
            "and month(update_at) = :month " +
            "and day(update_at) = :day " +
            "and deleted_at is null " +
            "and code like concat('%',:key,'%') " +
            "or name like concat('%',:key,'%') " +
            "or address like concat('%',:key,'%') or " +
            "phone like concat('%',:key,'%') or " +
            "mail like concat('%',:key,'%')",nativeQuery = true)
    List<InventoriesEntity> findWithoutYear(@Param("key") String key,@Param("day") String day,@Param("month") String month);

    @Query(value = "SELECT * FROM inventories where " +
            " month(update_at) = :month " +
            "and day(update_at) = :day " +
            "and year(update_at) = :year " +
            "and deleted_at is null or month(create_at) = :month" +
            " and year(create_at) = :year and day(create_at) = :day and deleted_at is null order by update_at desc" ,nativeQuery = true)
    List<InventoriesEntity> findWithTime(@Param("year") String year,@Param("month") String month,@Param("day") String day);

    @Override
    void deleteById(Integer integer);

    @Override
    <S extends InventoriesEntity> S save(S s);

    @Query(value = "select * from inventories where deleted_at is null " +
            "order by create_at desc\n" +
            " limit :offset,:limit ",nativeQuery = true)
    List<InventoriesEntity> pagination(@Param("offset") int offset,@Param("limit") int limit);

    @Query(value = "select count(*) from inventories where deleted_at is null",nativeQuery = true)
    long countAll();

    @Query(value = "select year(create_at) from inventories where create_at is not null " +
            "group by year(create_at) order by year(create_at) ",nativeQuery = true)
    List<Long> year();
    @Query(value = "select month(create_at) from inventories where create_at is not null " +
            "group by month(create_at) order by month(create_at) ",nativeQuery = true)
    List<Long> month();
    @Query(value = "select day(create_at) from inventories where create_at is not null " +
            "group by day(create_at) order by day(create_at)",nativeQuery = true)
    List<Long> day();

    @Query(value = "select * from inventories where month(create_at) = :month and deleted_at is null ",nativeQuery = true)
    List<InventoriesEntity> findMonth(@Param("month") int month);

    @Query(value = "select * from inventories where year(create_at) = :year and deleted_at is null  ",nativeQuery = true)
    List<InventoriesEntity> findYear(@Param("year") int year);

    @Query(value = "select * from inventories where day(create_at) = :day and deleted_at is null  ",nativeQuery = true)
    List<InventoriesEntity> findDay(@Param("day") int day);

    @Query(value = "select * from inventories where name = :name",nativeQuery = true)
    List<InventoriesEntity> findByName(@Param("name") String name);

    @Query(value = "select * from inventories where code like concat('%',:key,'%') or name like concat('%',:key,'%')",nativeQuery = true)
    List<InventoriesEntity> findForDetail(@Param("key") String key);

    @Query(value = "select * from inventories where code = :key or name = :key",nativeQuery = true)
    InventoriesEntity findByCodeOrName(String key);

    @Query(value = "SELECT * FROM inventories order by id desc limit 1",nativeQuery = true)
    InventoriesEntity findLastElement();

}
