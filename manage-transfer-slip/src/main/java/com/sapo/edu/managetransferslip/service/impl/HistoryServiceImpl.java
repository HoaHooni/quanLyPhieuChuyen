package com.sapo.edu.managetransferslip.service.impl;

import com.sapo.edu.managetransferslip.model.dto.HistoryDTO;
import com.sapo.edu.managetransferslip.model.dto.InventoriesDTO;
import com.sapo.edu.managetransferslip.model.dto.TransferDTO;
import com.sapo.edu.managetransferslip.model.dto.UsersDTO;
import com.sapo.edu.managetransferslip.model.entity.HistoryEntity;
import com.sapo.edu.managetransferslip.model.entity.TransferEntity;
import com.sapo.edu.managetransferslip.repository.HistoryRepository;
import com.sapo.edu.managetransferslip.repository.InventoryRepository;
import com.sapo.edu.managetransferslip.repository.TransferRepository;
import com.sapo.edu.managetransferslip.repository.UserRepository;
import com.sapo.edu.managetransferslip.service.HistoryServices;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryServices {
    private TransferRepository transferRepository;
    private InventoryRepository inventoryRepository;
    private UserRepository userRepository;
    private HistoryRepository historyRepository;

    public HistoryServiceImpl(TransferRepository transferRepository, InventoryRepository inventoryRepository, UserRepository userRepository, HistoryRepository historyRepository) {
        this.transferRepository = transferRepository;
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
    }


    @Override
    public List<HistoryDTO> getData(int id, int page, int limit) {
        int start = (page - 1) * limit;
        List<HistoryEntity> historyEntities = historyRepository.paginationHistoryOfInventory(id, start, limit);
        if (historyEntities.isEmpty()) {
            return null;
        }
        List<HistoryDTO> historyDTOS = new ArrayList<>();
        for (HistoryEntity historyEntity :
                historyEntities) {
            TransferEntity transferEntity = transferRepository.findById(historyEntity.getTransferId());
            UsersDTO usersDTO = new UsersDTO(historyEntity.getUser());
            historyDTOS.add(new HistoryDTO(new InventoriesDTO(transferEntity.getInventoryInput()),
                    new InventoriesDTO(transferEntity.getInventoryOutput()),
                    new TransferDTO(transferEntity),
                    convertStatus(historyEntity.getAction()), historyEntity.getDate(),
                    usersDTO));
        }
        return historyDTOS;
    }

    @Override
    public long sizeHistory(int id) {
        long size = 0;
        try {
            size = historyRepository.countPagination(id);

        } catch (Exception e) {
            return 0;
        }
        return size;
    }

    @Override
    public List<HistoryDTO> finding(int id, String key, int page, int limit) {

        int start = (page - 1) * limit;
        List<HistoryEntity> historyEntities = historyRepository.paginationSearch(id, key, start, limit);
        if (historyEntities.isEmpty()) {
            return null;
        }
        List<HistoryDTO> historyDTOS = new ArrayList<>();
        for (HistoryEntity historyEntity :
                historyEntities) {
            TransferEntity transferEntity = transferRepository.findById(historyEntity.getTransferId());
            historyDTOS.add(new HistoryDTO(new InventoriesDTO(transferEntity.getInventoryInput()),
                    new InventoriesDTO(transferEntity.getInventoryOutput())
                    , new TransferDTO(transferEntity), convertStatus(historyEntity.getAction()), historyEntity.getDate()
                    , new UsersDTO(historyEntity.getUser())));
        }
        return historyDTOS;
    }

    @Override
    public long sizeFinding(int id, String key) {
        long size = 0;
        try {
            size = historyRepository.sizeFinding(id, key);
        } catch (Exception e) {
            return 0;
        }
        return size;
    }

    @Override
    public List<HistoryDTO> findingByTime(int id, String key, String from, String to, int page, int limit) {
        System.out.println("id: " + id);
        System.out.println("key: " + key);
        System.out.println("page: " + page);
        System.out.println("limit: " + limit);
        System.out.println("from: " + from);
        System.out.println("to: " + to);
        int start = (page - 1) * limit;
        List<HistoryEntity> historyEntities = historyRepository.findingByTime(id, key, from, to, start, limit);
        if (historyEntities.isEmpty()) {
            return null;
        }
        List<HistoryDTO> historyDTOS = new ArrayList<>();
        for (HistoryEntity historyEntity :
                historyEntities) {
            TransferEntity transferEntity = transferRepository.findById(historyEntity.getTransferId());
            historyDTOS.add(new HistoryDTO(new InventoriesDTO(transferEntity.getInventoryInput()),
                    new InventoriesDTO(transferEntity.getInventoryOutput())
                    , new TransferDTO(transferEntity), convertStatus(historyEntity.getAction()), historyEntity.getDate()
                    , new UsersDTO(historyEntity.getUser())));
        }
        return historyDTOS;

    }

    @Override
    public long sizeByTime(int id, String key, String from, String to) {
        long size = 0;
        try {
            size = historyRepository.sizeByTime(id, key, from, to);
        } catch (Exception e) {
            return 0;
        }
        return size;
    }

    @Override
    public List<HistoryDTO> getDataImport(int id, int page, int limit) {
        int start = (page - 1) * limit;
        List<HistoryEntity> historyEntities = historyRepository.paginationHistoryImport(id, start, limit);
        if (historyEntities.isEmpty()) {
            System.out.println("null ne");
            return null;
        }
        List<HistoryDTO> historyDTOS = new ArrayList<>();
        for (HistoryEntity historyEntity :
                historyEntities) {
            TransferEntity transferEntity = transferRepository.findById(historyEntity.getTransferId());
            UsersDTO usersDTO = new UsersDTO(historyEntity.getUser());
            historyDTOS.add(new HistoryDTO(new InventoriesDTO(transferEntity.getInventoryInput()),
                    new InventoriesDTO(transferEntity.getInventoryOutput()),
                    new TransferDTO(transferEntity),
                    convertStatus(historyEntity.getAction()), historyEntity.getDate(),
                    usersDTO));
        }
        return historyDTOS;
    }

    @Override
    public long sizeImport(int id) {
        long size = 0;
        try {
            size = historyRepository.countImport(id);

        } catch (Exception e) {
            return 0;
        }
        return size;
    }

    @Override
    public List<HistoryDTO> findingImport(int id, String key, int page, int limit) {
        int start = (page - 1) * limit;
        List<HistoryEntity> historyEntities = historyRepository.searchImport(id, key, start, limit);
        if (historyEntities.isEmpty()) {
            return null;
        }
        List<HistoryDTO> historyDTOS = new ArrayList<>();
        for (HistoryEntity historyEntity :
                historyEntities) {
            TransferEntity transferEntity = transferRepository.findById(historyEntity.getTransferId());
            historyDTOS.add(new HistoryDTO(new InventoriesDTO(transferEntity.getInventoryInput()),
                    new InventoriesDTO(transferEntity.getInventoryOutput())
                    , new TransferDTO(transferEntity), convertStatus(historyEntity.getAction()), historyEntity.getDate()
                    , new UsersDTO(historyEntity.getUser())));
        }
        return historyDTOS;
    }

    @Override
    public long sizeFindingImport(int id, String key) {
        long size = 0;
        try {
            size = historyRepository.sizeSearchImport(id, key);
        } catch (Exception e) {
            return 0;
        }
        return size;
    }

    @Override
    public List<HistoryDTO> findingByTimeImport(int id, String key, String from, String to, int page, int limit) {

        int start = (page - 1) * limit;
        List<HistoryEntity> historyEntities = historyRepository.findingByTimeImport(id, key, from, to, start, limit);
        if (historyEntities.isEmpty()) {
            return null;
        }
        List<HistoryDTO> historyDTOS = new ArrayList<>();
        for (HistoryEntity historyEntity :
                historyEntities) {
            TransferEntity transferEntity = transferRepository.findById(historyEntity.getTransferId());
            historyDTOS.add(new HistoryDTO(new InventoriesDTO(transferEntity.getInventoryInput()),
                    new InventoriesDTO(transferEntity.getInventoryOutput())
                    , new TransferDTO(transferEntity), convertStatus(historyEntity.getAction()), historyEntity.getDate()
                    , new UsersDTO(historyEntity.getUser())));
        }
        return historyDTOS;
    }

    @Override
    public long sizeByTimeImport(int id, String key, String from, String to) {
        long size = 0;
        try {
            size = historyRepository.sizeByTimeImport(id, key, from, to);
        } catch (Exception e) {
            return 0;
        }
        return size;
    }

    @Override
    public List<HistoryDTO> getDataExport(int id, int page, int limit) {
        int start = (page - 1) * limit;
        List<HistoryEntity> historyEntities = historyRepository.paginationHistoryExport(id, start, limit);
        if (historyEntities.isEmpty()) {
            return null;
        }
        List<HistoryDTO> historyDTOS = new ArrayList<>();
        for (HistoryEntity historyEntity :
                historyEntities) {
            TransferEntity transferEntity = transferRepository.findById(historyEntity.getTransferId());
            UsersDTO usersDTO = new UsersDTO(historyEntity.getUser());
            historyDTOS.add(new HistoryDTO(new InventoriesDTO(transferEntity.getInventoryInput()),
                    new InventoriesDTO(transferEntity.getInventoryOutput()),
                    new TransferDTO(transferEntity),
                    convertStatus(historyEntity.getAction()), historyEntity.getDate(),
                    usersDTO));
        }
        return historyDTOS;
    }

    @Override
    public long sizeExport(int id) {
        long size = 0;
        try {
            size = historyRepository.countExport(id);

        } catch (Exception e) {
            return 0;
        }
        return size;
    }

    @Override
    public List<HistoryDTO> findingExport(int id, String key, int page, int limit) {
        int start = (page - 1) * limit;
        List<HistoryEntity> historyEntities = historyRepository.searchExport(id, key, start, limit);
        if (historyEntities.isEmpty()) {
            return null;
        }
        List<HistoryDTO> historyDTOS = new ArrayList<>();
        for (HistoryEntity historyEntity :
                historyEntities) {
            TransferEntity transferEntity = transferRepository.findById(historyEntity.getTransferId());
            historyDTOS.add(new HistoryDTO(new InventoriesDTO(transferEntity.getInventoryInput()),
                    new InventoriesDTO(transferEntity.getInventoryOutput())
                    , new TransferDTO(transferEntity), convertStatus(historyEntity.getAction()), historyEntity.getDate()
                    , new UsersDTO(historyEntity.getUser())));
        }
        return historyDTOS;
    }

    @Override
    public long sizeFindingExport(int id, String key) {
        long size = 0;
        try {
            size = historyRepository.sizeSearchExport(id, key);
        } catch (Exception e) {
            return 0;
        }
        return size;
    }

    @Override
    public List<HistoryDTO> findingByTimeExport(int id, String key, String from, String to, int page, int limit) {

        int start = (page - 1) * limit;
        List<HistoryEntity> historyEntities = historyRepository.findingByTimeExport(id, key, from, to, start, limit);
        if (historyEntities.isEmpty()) {
            return null;
        }
        List<HistoryDTO> historyDTOS = new ArrayList<>();
        for (HistoryEntity historyEntity :
                historyEntities) {
            TransferEntity transferEntity = transferRepository.findById(historyEntity.getTransferId());
            historyDTOS.add(new HistoryDTO(new InventoriesDTO(transferEntity.getInventoryInput()),
                    new InventoriesDTO(transferEntity.getInventoryOutput())
                    , new TransferDTO(transferEntity), convertStatus(historyEntity.getAction()), historyEntity.getDate()
                    , new UsersDTO(historyEntity.getUser())));
        }
        return historyDTOS;
    }

    @Override
    public long sizeByTimeExport(int id, String key, String from, String to) {
        long size = 0;
        try {
            size = historyRepository.sizeByTimeExport(id, key, from, to);
        } catch (Exception e) {
            return 0;
        }
        return size;
    }

    private String convertStatus(int status) {
        String statusToString = "";
        switch (status) {
            case 1:
                statusToString = "Chờ chuyển";
                break;
            case 2:
                statusToString = "Đang chuyển";
                break;
            case 3:
                statusToString = "Đã nhận";
                break;
            case 4:
                statusToString = "Đã hủy";
                break;
        }
        return statusToString;

    }


}
