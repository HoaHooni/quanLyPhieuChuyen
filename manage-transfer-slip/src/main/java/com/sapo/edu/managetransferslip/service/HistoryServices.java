package com.sapo.edu.managetransferslip.service;

import com.sapo.edu.managetransferslip.model.dto.HistoryDTO;


import java.util.List;

public interface HistoryServices {
    List<HistoryDTO> getData(int id, int page, int limit);

    long sizeHistory(int id);

    List<HistoryDTO> finding(int id,String key,int page,int limit);

    long sizeFinding(int id,String key);

    List<HistoryDTO> findingByTime(int id,String key,String from,String to,int page,int limit);

    long sizeByTime(int id,String key,String from,String to);

    //import
    List<HistoryDTO> getDataImport(int id, int page, int limit);

    long sizeImport(int id);

    List<HistoryDTO> findingImport(int id,String key,int page,int limit);

    long sizeFindingImport(int id,String key);

    List<HistoryDTO> findingByTimeImport(int id,String key,String from,String to,int page,int limit);

    long sizeByTimeImport(int id,String key,String from,String to);

    //export
    List<HistoryDTO> getDataExport(int id, int page, int limit);

    long sizeExport(int id);

    List<HistoryDTO> findingExport(int id,String key,int page,int limit);

    long sizeFindingExport(int id,String key);

    List<HistoryDTO> findingByTimeExport(int id,String key,String from,String to,int page,int limit);

    long sizeByTimeExport(int id,String key,String from,String to);

}
