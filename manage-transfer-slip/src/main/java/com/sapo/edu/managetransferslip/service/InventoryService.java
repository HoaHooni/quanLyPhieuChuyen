package com.sapo.edu.managetransferslip.service;


import com.sapo.edu.managetransferslip.model.dto.InventoriesDTO;
import com.sapo.edu.managetransferslip.model.entity.InventoriesEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface InventoryService {

    //custom data
    List<InventoriesDTO> getData();

    //done
    ResponseEntity<InventoriesDTO> add(InventoriesDTO inventoriesDTO);
    ResponseEntity<InventoriesDTO> addAll(List<InventoriesDTO> inventoriesDTOS);

    //done

    ResponseEntity<InventoriesDTO> delete(int id);


    //done

    InventoriesDTO find(int id);

    //find by key
    List<InventoriesDTO> findByKey(String key,int page, int limit);
    //count finding
    long countFinding(String key);

    //find by key exact
    List<InventoriesDTO> findExact(String key);

    //done
    ResponseEntity<InventoriesDTO> update(InventoriesDTO inventoriesDTO);
    ResponseEntity<InventoriesDTO> updateAll(List<InventoriesDTO> inventoriesDTOS);

    List<InventoriesDTO> pagination(int page,int limit);

    long countAll();

    InventoriesDTO findByCode(String code);

    List<Long> year();

    List<Long> month();

    List<Long> day();

    List<InventoriesDTO> findYear(int year);

    List<InventoriesDTO> findMonth(int month);

    List<InventoriesDTO> findDay(int day);

    List<InventoriesDTO> findFilter(String key,String year,String month,String day);

    List<InventoriesDTO> findWithoutDay(String key,String year,String month);

    List<InventoriesDTO> findWithoutMonth(String key,String year,String day);

    List<InventoriesDTO> findWithoutYear(String key,String month,String day);

    List<InventoriesDTO> findWithTime(String year, String month, String day);

}
