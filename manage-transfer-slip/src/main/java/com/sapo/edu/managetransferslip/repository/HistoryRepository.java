package com.sapo.edu.managetransferslip.repository;

import com.sapo.edu.managetransferslip.model.entity.HistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Integer> {
    List<HistoryEntity> findAllByTransferId(int transferId);
    Page<HistoryEntity> findAllByTransferId(int transferId, Pageable pageable);

    //export
    @Query(value = "SELECT DISTINCT hs.id,hs.action,hs.date,hs.note,hs.user_id,hs.transfer_id\n" +
            "FROM history as hs INNER JOIN transfer ON (hs.transfer_id = transfer.id) and (transfer.inventory_output_id  = :id ) \n" +
            "order by date desc limit :page,:limit \n" ,nativeQuery = true)
    List<HistoryEntity> paginationHistoryExport(@Param("id") int id,@Param("page")int page,@Param("limit") int limit);

    @Query(value = "SELECT DISTINCT count(*)" +
            "FROM history as hs INNER JOIN transfer ON (hs.transfer_id = transfer.id) and (transfer.inventory_output_id  = :id ) \n"
            ,nativeQuery = true)
    long countExport(@Param("id") int id);

    @Query(value = "SELECT DISTINCT hs.id,hs.action,hs.date,hs.note,hs.user_id,hs.transfer_id from history as hs\n" +
            "left join users as u on u.id = hs.user_id\n" +
            "left join transfer as t on t.id = hs.transfer_id\n" +
            "join inventories as i on (t.inventory_output_id = i.id or t.inventory_input_id = i.id)\n" +
            "where (u.code like concat('%',:key,'%') or u.username like concat('%',:key,'%') or i.code like concat('%',:key,'%') or i.name like concat('%',:key,'%')\n" +
            "or t.code like concat('%',:key,'%')) and (t.inventory_output_id = :id) order by hs.date limit :page,:limit\n",nativeQuery = true)
    List<HistoryEntity> searchExport(@Param("id")int id,@Param("key") String key,@Param("page")int page,@Param("limit") int limit);

    @Query(value = "SELECT DISTINCT count(*) from history as hs\n" +
            "left join users as u on u.id = hs.user_id\n" +
            "left join transfer as t on t.id = hs.transfer_id\n" +
            "join inventories as i on (t.inventory_output_id = i.id or t.inventory_input_id = i.id)\n" +
            "where (u.code like concat('%',:key,'%') or u.username like concat('%',:key,'%') or i.code like concat('%',:key,'%') or i.name like concat('%',:key,'%')\n" +
            "or t.code like concat('%',:key,'%')) and (t.inventory_output_id = :id)",nativeQuery = true)
    long sizeSearchExport(@Param("id") int id,@Param("key") String key);

    @Query(value = "SELECT DISTINCT hs.id,hs.action,hs.date,hs.note,hs.user_id,hs.transfer_id from history as hs\n" +
            "left join users as u on u.id = hs.user_id\n" +
            "left join transfer as t on t.id = hs.transfer_id\n" +
            "join inventories as i on (t.inventory_output_id = i.id or t.inventory_input_id = i.id)\n" +
            "where (u.code like concat('%',:key,'%') or u.username like concat('%',:key,'%') or i.code like concat('%',:key,'%') or i.name like concat('%',:key,'%')\n" +
            "or t.code like concat('%',:key,'%')) and(hs.date between :from and :to) and (t.inventory_output_id = :id) order by hs.date desc limit :page,:limit",nativeQuery = true)
    List<HistoryEntity> findingByTimeExport(@Param("id") int id,@Param("key") String key,@Param("from") String from,@Param("to") String to,@Param("page") int page,@Param("limit") int limit);

    @Query(value = "SELECT DISTINCT count(*) from history as hs\n" +
            "left join users as u on u.id = hs.user_id\n" +
            "left join transfer as t on t.id = hs.transfer_id\n" +
            "join inventories as i on (t.inventory_output_id = i.id or t.inventory_input_id = i.id)\n" +
            "where (u.code like concat('%',:key,'%') or u.username like concat('%',:key,'%') or i.code like concat('%',:key,'%') or i.name like concat('%',:key,'%')\n" +
            "or t.code like concat('%',:key,'%')) and(hs.date between :from and :to) and (t.inventory_output_id = :id) order by hs.date desc limit :page,:limit",nativeQuery = true)
    long sizeByTimeExport(@Param("id") int id,@Param("key") String key,@Param("from") String from,@Param("to") String to);

    //import
    @Query(value = "SELECT DISTINCT hs.id,hs.action,hs.date,hs.note,hs.user_id,hs.transfer_id\n" +
            "FROM history as hs INNER JOIN transfer ON (hs.transfer_id = transfer.id) and (transfer.inventory_input_id  = :id ) \n" +
            "order by date desc limit :page,:limit \n" ,nativeQuery = true)
    List<HistoryEntity> paginationHistoryImport(@Param("id") int id,@Param("page")int page,@Param("limit") int limit);

    @Query(value = "SELECT DISTINCT count(*)" +
            "FROM history as hs INNER JOIN transfer ON (hs.transfer_id = transfer.id) and (transfer.inventory_input_id  = :id ) \n"
            ,nativeQuery = true)
    long countImport(@Param("id") int id);

    @Query(value = "SELECT DISTINCT hs.id,hs.action,hs.date,hs.note,hs.user_id,hs.transfer_id from history as hs\n" +
            "left join users as u on u.id = hs.user_id\n" +
            "left join transfer as t on t.id = hs.transfer_id\n" +
            "join inventories as i on (t.inventory_output_id = i.id or t.inventory_input_id = i.id)\n" +
            "where (u.code like concat('%',:key,'%') or u.username like concat('%',:key,'%') or i.code like concat('%',:key,'%') or i.name like concat('%',:key,'%')\n" +
            "or t.code like concat('%',:key,'%')) and (t.inventory_input_id = :id ) order by hs.date limit :page,:limit\n",nativeQuery = true)
    List<HistoryEntity> searchImport(@Param("id")int id,@Param("key") String key,@Param("page")int page,@Param("limit") int limit);

    @Query(value = "SELECT DISTINCT count(*) from history as hs\n" +
            "left join users as u on u.id = hs.user_id\n" +
            "left join transfer as t on t.id = hs.transfer_id\n" +
            "join inventories as i on (t.inventory_output_id = i.id or t.inventory_input_id = i.id)\n" +
            "where (u.code like concat('%',:key,'%') or u.username like concat('%',:key,'%') or i.code like concat('%',:key,'%') or i.name like concat('%',:key,'%')\n" +
            "or t.code like concat('%',:key,'%')) and (t.inventory_input_id = :id )",nativeQuery = true)
    long sizeSearchImport(@Param("id") int id,@Param("key") String key);

    @Query(value = "SELECT DISTINCT hs.id,hs.action,hs.date,hs.note,hs.user_id,hs.transfer_id from history as hs\n" +
            "left join users as u on u.id = hs.user_id\n" +
            "left join transfer as t on t.id = hs.transfer_id\n" +
            "join inventories as i on (t.inventory_output_id = i.id or t.inventory_input_id = i.id)\n" +
            "where (u.code like concat('%',:key,'%') or u.username like concat('%',:key,'%') or i.code like concat('%',:key,'%') or i.name like concat('%',:key,'%')\n" +
            "or t.code like concat('%',:key,'%')) and(hs.date between :from and :to) and (t.inventory_input_id = :id ) order by hs.date desc limit :page,:limit",nativeQuery = true)
    List<HistoryEntity> findingByTimeImport(@Param("id") int id,@Param("key") String key,@Param("from") String from,@Param("to") String to,@Param("page") int page,@Param("limit") int limit);

    @Query(value = "SELECT DISTINCT count(*) from history as hs\n" +
            "left join users as u on u.id = hs.user_id\n" +
            "left join transfer as t on t.id = hs.transfer_id\n" +
            "join inventories as i on (t.inventory_output_id = i.id or t.inventory_input_id = i.id)\n" +
            "where (u.code like concat('%',:key,'%') or u.username like concat('%',:key,'%') or i.code like concat('%',:key,'%') or i.name like concat('%',:key,'%')\n" +
            "or t.code like concat('%',:key,'%')) and(hs.date between :from and :to) and (t.inventory_input_id = :id ) order by hs.date desc limit :page,:limit",nativeQuery = true)
    long sizeByTimeImport(@Param("id") int id,@Param("key") String key,@Param("from") String from,@Param("to") String to);

    //all
    @Query(value = "SELECT DISTINCT hs.id,hs.action,hs.date,hs.note,hs.user_id,hs.transfer_id\n" +
            "FROM history as hs INNER JOIN transfer ON (hs.transfer_id = transfer.id) and (transfer.inventory_output_id  = :id or transfer.inventory_output_id = :id) \n" +
            "order by date desc limit :page,:limit \n" ,nativeQuery = true)
    List<HistoryEntity> paginationHistoryOfInventory(@Param("id") int id,@Param("page")int page,@Param("limit") int limit);

    @Query(value = "SELECT DISTINCT count(*)" +
            "FROM history as hs INNER JOIN transfer ON (hs.transfer_id = transfer.id) and (transfer.inventory_output_id  = :id or transfer.inventory_output_id = :id) \n"
             ,nativeQuery = true)
    long countPagination(@Param("id") int id);

    @Query(value = "SELECT DISTINCT  hs.id,hs.action,hs.date,hs.note,hs.user_id,hs.transfer_id from history as hs\n" +
            "left join users as u on u.id = hs.user_id\n" +
            "left join transfer as t on t.id = hs.transfer_id\n" +
            "join inventories as i on (t.inventory_input_id = i.id or t.inventory_output_id = i.id)\n" +
            "where (u.code like concat('%',:key,'%') or u.username like concat('%',:key,'%') or i.code like concat('%',:key,'%') or i.name like concat('%',:key,'%')" +
            "or t.code like concat('%', :key,'%')) and (t.inventory_output_id = :id or t.inventory_input_id = :id) order by hs.date limit :page,:limit\n",nativeQuery = true)
    List<HistoryEntity> paginationSearch(@Param("id")int id,@Param("key") String key,@Param("page")int page,@Param("limit") int limit);

    @Query(value = "SELECT DISTINCT  count(*) from history as hs\n" +
            "left join users as u on u.id = hs.user_id\n" +
            "left join transfer as t on t.id = hs.transfer_id\n" +
            "join inventories as i on (t.inventory_input_id = i.id or t.inventory_output_id = i.id)\n" +
            "where (u.code like concat('%',:key,'%') or u.username like concat('%',:key,'%') or i.code like concat('%',:key,'%') or i.name like concat('%',:key,'%')\n" +
            "or t.code like concat('%', :key,'%')) and (t.inventory_output_id = :id or t.inventory_input_id = :id)",nativeQuery = true)
    long sizeFinding(@Param("id") int id,@Param("key") String key);

    @Query(value = "SELECT DISTINCT hs.id,hs.action,hs.date,hs.note,hs.user_id,hs.transfer_id from history as hs\n" +
            "left join users as u on u.id = hs.user_id\n" +
            "left join transfer as t on t.id = hs.transfer_id\n" +
            "join inventories as i on (t.inventory_input_id = i.id or t.inventory_output_id = i.id)\n" +
            "where (u.code like concat('%',:key,'%') or u.username like concat('%',:key,'%') or i.code like concat('%',:key,'%') or i.name like concat('%',:key,'%')\n" +
            "or t.code like concat('%',:key,'%')) and(hs.date between :from and :to) and (t.inventory_input_id = :id or t.inventory_output_id = :id) order by hs.date desc limit :page,:limit",nativeQuery = true)
    List<HistoryEntity> findingByTime(@Param("id") int id,@Param("key") String key,@Param("from") String from,@Param("to") String to,@Param("page") int page,@Param("limit") int limit);

    @Query(value = "SELECT DISTINCT count(*) from history as hs\n" +
            "left join users as u on u.id = hs.user_id\n" +
            "left join transfer as t on t.id = hs.transfer_id\n" +
            "join inventories as i on (t.inventory_input_id = i.id or t.inventory_output_id = i.id)\n" +
            "where (u.code like concat('%',:key,'%') or u.username like concat('%',:key,'%') or i.code like concat('%',:key,'%') or i.name like concat('%',:key,'%')\n" +
            "or t.code like concat('%',:key,'%')) and(hs.date between :from and :to) and (t.inventory_input_id = :id or t.inventory_output_id = :id) order by hs.date desc limit :page,:limit",nativeQuery = true)
    long sizeByTime(@Param("id") int id,@Param("key") String key,@Param("from") String from,@Param("to") String to);

}
