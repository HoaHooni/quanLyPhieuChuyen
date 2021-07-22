package com.sapo.edu.managetransferslip.service;

import com.sapo.edu.managetransferslip.model.dto.*;
import com.sapo.edu.managetransferslip.model.entity.TransferEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.List;

public interface TransferService {

    List<TransferDTO> findAllTransfer(Integer page, Integer limit);

    ResponseEntity<?> createTransfer(InputTransferDTO inputTransferDTO);

    ResponseEntity<?> updateTransfer(InputTransferDTO inputTransferDTO, Integer id);

    ResponseEntity<?> getDetail(Integer id);

    ResponseEntity<?> shipping(Integer id, Integer userId);


    ResponseEntity<?> receive(Integer id, Integer userId);


    Message deleteTransfer(Integer id, Integer userId);

    List<TransferDTO> search(String keyword , Integer page, Integer limit);

    List<TransferDTO> findByDate(Date dateMin, Date dateMax, Integer page, Integer limit);

    List<TransferDTO> findHistory(int id,int pages,int limit);

    List<TransferDTO> findByInventoryInputId(int id,int page,int limit);

    List<TransferDTO> findByInventoryOutputId(int id,int page,int limit);

    List<TransferDTO> findingByKeyForHistory(String key);

    long sizeHistory(int id);

    long sizeExport(int id);

    long sizeImport(int id);


    long sizeFindingBetween(String from,String to,int id);

    long sizeSearch(String key,int id);


    List<HistoryTransferDTO> historyTransfer(int transferId, Integer page, Integer limit);

    long countAll();

    List<TransferDTO> findAllByInventoryAndDate(String key, Date date, int inventory,PaginationDto page);

}
