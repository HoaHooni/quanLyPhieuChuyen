package com.sapo.edu.managetransferslip.repository;

import com.sapo.edu.managetransferslip.model.entity.TransferEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity,Integer> {

    TransferEntity findById(int id);

    Boolean existsByCode(String code);

    TransferEntity findFirstByOrderByIdDesc();



    List<TransferEntity> findAllByOrderByIdDesc();
    Page<TransferEntity> findAllByOrderByIdDesc(Pageable pageable);

    List<TransferEntity> findAllByDeletedAtIsNullAndCodeLikeOrderByIdDesc(String code);
    Page<TransferEntity> findAllByDeletedAtIsNullAndCodeLikeOrderByIdDesc(String code, Pageable pageable);


    @Query(value = "SELECT t.* FROM transfer t \n" +
            "join users u on t.user_id = u.id\n" +
            "join inventories i1 on t.inventory_input_id = i1.id\n" +
            "join inventories i2 on t.inventory_output_id = i2.id\n" +
            "where (i1.name like %:key% or i2.name like %:key% or u.username like %:key% or t.code like %:key%) " +
            "order by t.id desc ",nativeQuery = true)
    List<TransferEntity> findAllByKey(@Param("key") String key);

    @Query(value = "SELECT t.* FROM transfer t \n" +
            "join users u on t.user_id = u.id\n" +
            "join inventories i1 on t.inventory_input_id = i1.id\n" +
            "join inventories i2 on t.inventory_output_id = i2.id\n" +
            "where (i1.name like %:key% or i2.name like %:key% or u.username like %:key% or t.code like %:key%)" +
            "order by t.id desc  limit :page, :limit",nativeQuery = true)
    List<TransferEntity> findAllByKey(@Param("key") String key,@Param("page") int page,@Param("limit") int limit);

    @Query(value = "Select * from transfer where inventory_input_id = :id or inventory_output_id = :id order by create_at " +
            " limit :offset,:limit",nativeQuery = true)
    List<TransferEntity> findHistoryByInventoryId(@Param("id") int id,@Param("offset") int offset,@Param("limit") int limit);

    @Query(value = "Select * from transfer where inventory_input_id = :id order by create_at limit :offset,:limit",nativeQuery = true)
    List<TransferEntity> findByInventoryInputId(@Param("id") int id,@Param("offset") int offset,@Param("limit") int limit);

    @Query(value = "SELECT * FROM transfer where inventory_output_id = :id order by create_at limit :offset,:limit",nativeQuery = true)
    List<TransferEntity> findByInventoryOutputId(@Param("id") int id,@Param("offset") int offset,@Param("limit") int limit);

    @Query(value =  "Select count(*) from transfer where inventory_input_id = :id or inventory_output_id = :id ",nativeQuery = true)
    long sizeHistory(@Param("id") int id);

    @Query(value = "Select count(*) from transfer where inventory_input_id = :id ",nativeQuery = true)
    long sizeImport(@Param("id") int id);

    @Query(value = "Select count(*) from transfer where inventory_output_id = :id",nativeQuery = true)
    long sizeExport(@Param("id") int id);

    @Query(value = "SELECT * FROM transfer where (update_at between :from  and :to) and (inventory_input_id= :id or inventory_output_id = :id) order by update_at desc limit :offset,:limit"
            ,nativeQuery = true)
    List<TransferEntity> paginationFindingBetween(@Param("from") String from,@Param("to") String to,@Param("offset") int offset,@Param("limit") int limit, @Param("id") int id);

    @Query(value = "SELECT count(*) FROM transfer where (inventory_input_id = :id or inventory_output_id = :id) and (update_at between :from and :to)",nativeQuery = true)
    long countFindingBetWeen(@Param("from") String from,@Param("to") String to,@Param("id")int id);

    @Query(value = "SELECT t.* FROM transfer t \n" +
            "join users u on t.user_id = u.id\n" +
            "join inventories i1 on t.inventory_input_id = i1.id\n" +
            "join inventories i2 on t.inventory_output_id = i2.id\n" +
            "where (i1.name like %:key% or i2.name like %:key% or t.code like %:key%) and t.deleted_at is null and (t.inventory_input_id = :id or t.inventory_output_id = :id)" +
            "order by t.id desc limit :offset,:limit ",nativeQuery = true)
    List<TransferEntity> findingByKeyAndId(@Param("offset") int offset,@Param("limit") int limit,@Param("key") String key,@Param("id") int id);

    @Query(value = "SELECT count(*) FROM transfer t \n" +
            "join users u on t.user_id = u.id\n" +
            "join inventories i1 on t.inventory_input_id = i1.id\n" +
            "join inventories i2 on t.inventory_output_id = i2.id\n" +
            "where (i1.name like %:key% or i2.name like %:key% or t.code like %:key%) and t.deleted_at is null and (t.inventory_input_id = :id or t.inventory_output_id = :id)" +
            "order by t.id desc ",nativeQuery = true)
    long sizeSearch(@Param("key") String key,@Param("id") int id);

    @Query(value = "Select * from transfer where status = :status and (inventory_input_id = :id or inventory_output_id = :id) order by update_at desc limit :page,:limit",nativeQuery = true)
    List<TransferEntity> findWithStatus(@Param("id") int id,@Param("status") int status,@Param("page") int page,@Param("limit") int limit);
    List<TransferEntity> findAllByCreateAtGreaterThanEqualAndCreateAtLessThanEqualOrderByIdDesc(Date dateMin, Date dateMax);
    Page<TransferEntity> findAllByCreateAtGreaterThanEqualAndCreateAtLessThanEqualOrderByIdDesc(Date dateMin, Date dateMax, Pageable pageable);

    @Query(value = "Select * from transfer where (inventory_input_id = :inventory or inventory_output_id = :inventory) and (Date(moving_at) = :date or Date(finish_at) =:date)", nativeQuery = true)
    List<TransferEntity> findAllByInventoryAndDate(@Param("date") Date date,@Param("inventory") int inventory);

}
