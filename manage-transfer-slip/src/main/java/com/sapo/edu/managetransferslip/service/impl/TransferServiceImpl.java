package com.sapo.edu.managetransferslip.service.impl;

import com.sapo.edu.managetransferslip.repository.ProductRepository;
import com.sapo.edu.managetransferslip.model.dto.*;
import com.sapo.edu.managetransferslip.model.entity.*;
import com.sapo.edu.managetransferslip.repository.*;
import com.sapo.edu.managetransferslip.service.TransferDetailService;
import com.sapo.edu.managetransferslip.service.TransferService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final TransferDetailRepository transferDetailRepository;
    private final UserRepository userRepository;
    private final InventoriesRepository inventoriesRepository;
    private final ProductRepository productRepository;
    private final InventoryDetailRepository inventoryDetailRepository;
    private final TransferDetailService transferDetailService;
    private final HistoryRepository historyRepository;

    public TransferServiceImpl(TransferRepository transferRepository, TransferDetailRepository transferDetailRepository, UserRepository userRepository, InventoriesRepository inventoriesRepository, ProductRepository productRepository, InventoryDetailRepository inventoryDetailRepository, TransferDetailService transferDetailService, HistoryRepository historyRepository) {
        this.transferRepository = transferRepository;
        this.transferDetailRepository = transferDetailRepository;
        this.userRepository = userRepository;
        this.inventoriesRepository = inventoriesRepository;
        this.productRepository = productRepository;
        this.inventoryDetailRepository = inventoryDetailRepository;
        this.transferDetailService = transferDetailService;
        this.historyRepository = historyRepository;
    }

    @Override
    public List<TransferDTO> findAllTransfer(Integer page, Integer limit) {
        List<TransferEntity> transferEntityList;
        if (page != null && limit != null) {
            Page<TransferEntity> transferEntityPage = transferRepository.findAllByOrderByIdDesc(PageRequest.of(page, limit));
            transferEntityList = transferEntityPage.toList();

        } else {
            transferEntityList = transferRepository.findAllByOrderByIdDesc();
        }
        List<TransferDTO> transferDTOList = new ArrayList<>();

        for (TransferEntity transferEntity : transferEntityList) {
            TransferDTO transferDTO = new TransferDTO(transferEntity);

            transferDTOList.add(transferDTO);
        }
        return transferDTOList;

    }

    @Override
    @Transactional
    public ResponseEntity<?> createTransfer(InputTransferDTO inputTransferDTO) {
        try {
            UsersEntity usersEntity = userRepository.getById(inputTransferDTO.getUser_id());
            InventoriesEntity inventoriesEntityInput = inventoriesRepository.getById(inputTransferDTO.getInventoryInputId());


            InventoriesEntity inventoriesEntityOutput = inventoriesRepository.getById(inputTransferDTO.getInventoryOutputId());

            List<TransferDetailEntity> transferDetailEntities = new ArrayList<>();

            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT);


            TransferEntity transferEntity = modelMapper.map(inputTransferDTO, TransferEntity.class);
            if (inputTransferDTO.getCode() == "") {
                TransferEntity transferEntity1 = transferRepository.findFirstByOrderByIdDesc();
                transferEntity.setCode(("TF" + String.valueOf(transferEntity1.getId() + 1)));
            } else {
                if (transferRepository.existsByCode(inputTransferDTO.getCode())) {
                    return ResponseEntity.badRequest().body(new Message("Error: Code da ton tai"));
                }
            }

            transferEntity.setStatus(1);
            transferEntity.setUser(usersEntity);
            transferEntity.setInventoryInput(inventoriesEntityInput);
            transferEntity.setInventoryOutput(inventoriesEntityOutput);
            transferEntity.setTransferDetailEntities(transferDetailEntities);
            //transferRepository.save(transferEntity);
            for (InputTransferDetailDTO inputTransferDetailDTO : inputTransferDTO.getInputTransferDetailDTOList()) {
                InventoryDetailEntity inventoryDetailEntityInput = inventoryDetailRepository.findWithElementId(inputTransferDetailDTO.getProductId(),
                        inventoriesEntityInput.getId());
                InventoryDetailEntity inventoryDetailEntityOutput = inventoryDetailRepository.findWithElementId(inputTransferDetailDTO.getProductId(),
                        inventoriesEntityOutput.getId());


                if (inputTransferDetailDTO.getTotal() > 0 && inputTransferDetailDTO.getTotal() <= inventoryDetailEntityOutput.getNumberPro()) {
                    TransferDetailEntity transferDetailEntity = modelMapper.map(inputTransferDetailDTO, TransferDetailEntity.class);
                    transferRepository.save(transferEntity);
                    transferDetailEntity.setTransfer(transferRepository.getById(transferEntity.getId()));
                    transferDetailEntity.setProduct(productRepository.getById(inputTransferDetailDTO.getProductId()));
                    transferDetailEntities.add(transferDetailEntity);
                    transferDetailRepository.save(transferDetailEntity);


                }
//                else if(inputTransferDetailDTO.getTotal() < 0) {
//                    return ResponseEntity.badRequest().body(new Message("TOTAL NEGATIVE "));
//
//                }
                else {
                    return ResponseEntity.badRequest().body(new Message("TOTAL PRODUCT INVALID"));

                }
            }
            transferEntity.setTransferDetailEntities(transferDetailEntities);
            transferRepository.save(transferEntity);
            HistoryEntity historyEntity = new HistoryEntity();
            historyEntity.setTransferId(transferEntity.getId());
            historyEntity.setAction(1);
            historyEntity.setDate(transferEntity.getCreateAt());
            historyEntity.setUser(usersEntity);
            historyEntity.setNote(transferEntity.getNote());
            historyRepository.save(historyEntity);


            TransferDTO transferDTO = new TransferDTO(transferEntity);

            return ResponseEntity.ok(transferDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new Message("ADD_TRANSFER_FAILED"));

        }


    }

    @Override
    public ResponseEntity<?> updateTransfer(InputTransferDTO inputTransferDTO, Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<?> getDetail(Integer id) {
        TransferEntity transferEntity = transferRepository.getById(id);


        TransferDTO transferDTO = new TransferDTO(transferEntity);

        return ResponseEntity.ok(transferDTO);
    }

    @Override
    public ResponseEntity<?> shipping(Integer id, Integer userId) {
        TransferEntity transferEntity = transferRepository.getById(id);

        if (transferEntity.getStatus() == 2) {
            return ResponseEntity.ok(new Message("Hàng đã chuyển đi không thể chuyển tiếp"));
        } else if (transferEntity.getStatus() == 3) {
            return ResponseEntity.ok(new Message("Đã nhận hàng không thể chuyển tiếp"));

        } else if (transferEntity.getStatus() == 4) {
            return ResponseEntity.ok(new Message("Đã hủy phiếu"));

        } else {
            for (TransferDetailEntity transferDetailEntity : transferEntity.getTransferDetailEntities()) {
                InventoryDetailEntity inventoryDetailEntityOutput = inventoryDetailRepository.findWithElementId(transferDetailEntity.getProduct().getId(),
                        transferEntity.getInventoryOutput().getId());
                if (transferDetailEntity.getTotal() > 0 && transferDetailEntity.getTotal() <= inventoryDetailEntityOutput.getNumberPro()) {
                    inventoryDetailEntityOutput.setNumberPro(inventoryDetailEntityOutput.getNumberPro() - transferDetailEntity.getTotal());
                    inventoryDetailRepository.save(inventoryDetailEntityOutput);
                    transferEntity.setStatus(2);
                    transferEntity.setMovingAt(new Timestamp(System.currentTimeMillis()));
                    transferRepository.save(transferEntity);
                    HistoryEntity historyEntity = new HistoryEntity();
                    historyEntity.setTransferId(transferEntity.getId());
                    historyEntity.setAction(2);
                    historyEntity.setDate(transferEntity.getMovingAt());
                    historyEntity.setUser(userRepository.getById(userId));
                    historyEntity.setNote(transferEntity.getNote());
                    historyRepository.save(historyEntity);


                } else {
                    return ResponseEntity.ok(new Message("TOTAL PRODUCT INVALID"));

                }

            }
            return ResponseEntity.ok(new Message("SUCCESS"));

        }
    }

    @Override
    public ResponseEntity<?> receive(Integer id, Integer userId) {
        TransferEntity transferEntity = transferRepository.getById(id);

        if (transferEntity.getStatus() == 3) {
            return ResponseEntity.ok(new Message("Đã nhận hàng rồi"));

        } else if (transferEntity.getStatus() == 1) {
            return ResponseEntity.ok(new Message("Chưa chuyển hàng đi"));
        } else if (transferEntity.getStatus() == 4) {
            return ResponseEntity.ok(new Message("Đã hủy phiếu"));

        } else {
            transferEntity.setStatus(3);
            transferEntity.setFinishAt(new Timestamp(System.currentTimeMillis()));
            transferRepository.save(transferEntity);
            HistoryEntity historyEntity = new HistoryEntity();
            historyEntity.setTransferId(transferEntity.getId());
            historyEntity.setAction(3);
            historyEntity.setDate(transferEntity.getFinishAt());
            historyEntity.setUser(userRepository.getById(userId));
            historyEntity.setNote(transferEntity.getNote());
            historyRepository.save(historyEntity);
            for (TransferDetailEntity transferDetailEntity : transferEntity.getTransferDetailEntities()) {
                InventoryDetailEntity inventoryDetailEntityInput = inventoryDetailRepository.findWithElementId(transferDetailEntity.getProduct().getId(),
                        transferEntity.getInventoryInput().getId());


                if (inventoryDetailEntityInput != null) {
                    inventoryDetailEntityInput.setNumberPro(inventoryDetailEntityInput.getNumberPro() + transferDetailEntity.getTotal());
                    inventoryDetailRepository.save(inventoryDetailEntityInput);
                } else {
                    InventoryDetailEntity inventoryDetailEntityInput1 = new InventoryDetailEntity(productRepository.getById(transferDetailEntity.getProduct().getId()),
                            transferEntity.getInventoryInput(), transferDetailEntity.getTotal());
                    inventoryDetailRepository.save(inventoryDetailEntityInput1);
                }


            }
            return ResponseEntity.ok(new Message("SUCCESS"));
        }


    }

    @Override
    public Message deleteTransfer(Integer id, Integer userId) {
        try {
            TransferEntity transferEntity = transferRepository.getById(id);
            if (transferEntity != null) {
                if (transferEntity.getStatus() == 1) {
                    transferEntity.setDeletedAt(new Timestamp(System.currentTimeMillis()));
                    transferEntity.setStatus(4);
                    transferRepository.save(transferEntity);
                    HistoryEntity historyEntity = new HistoryEntity();
                    historyEntity.setTransferId(transferEntity.getId());
                    historyEntity.setAction(4);
                    historyEntity.setDate(transferEntity.getDeletedAt());
                    historyEntity.setUser(userRepository.getById(userId));
                    historyEntity.setNote(transferEntity.getNote());
                    historyRepository.save(historyEntity);
                    return new Message("Delete successfully, transferId: " + id);
                } else if (transferEntity.getStatus() == 2) {
                    transferEntity.setDeletedAt(new Timestamp(System.currentTimeMillis()));
                    transferEntity.setStatus(4);
                    transferRepository.save(transferEntity);
                    HistoryEntity historyEntity = new HistoryEntity();
                    historyEntity.setTransferId(transferEntity.getId());
                    historyEntity.setAction(4);
                    historyEntity.setDate(transferEntity.getDeletedAt());
                    historyEntity.setUser(userRepository.getById(userId));
                    historyEntity.setNote(transferEntity.getNote());
                    historyRepository.save(historyEntity);
                    for (TransferDetailEntity transferDetailEntity : transferEntity.getTransferDetailEntities()) {
                        InventoryDetailEntity inventoryDetailEntityOutput = inventoryDetailRepository.findWithElementId(transferDetailEntity.getProduct().getId(),
                                transferEntity.getInventoryOutput().getId());

                        inventoryDetailEntityOutput.setNumberPro(inventoryDetailEntityOutput.getNumberPro() + transferDetailEntity.getTotal());
                        inventoryDetailRepository.save(inventoryDetailEntityOutput);

                    }
                    return new Message("Delete successfully, transferId: " + id);
                } else {
                    return new Message("Hàng đã chuyển đi không thể hủy phiếu");
                }

            } else {
                return new Message("transferId: " + id + " is not found.");
            }
        } catch (Exception e) {
            return new Message("Delete failed.");
        }
    }

    @Override
    public List<TransferDTO> search(String keyword, Integer page, Integer limit) {
        List<TransferEntity> transferEntityList;
        if (page != null && limit != null) {
            int start = page * limit;
            transferEntityList = transferRepository.findAllByKey(keyword, start, limit);
        } else {
            transferEntityList = transferRepository.findAllByKey(keyword);
        }
        List<TransferDTO> transferDTOList = new ArrayList<>();

        for (TransferEntity transferEntity : transferEntityList) {
            TransferDTO transferDTO = new TransferDTO(transferEntity);
            transferDTOList.add(transferDTO);
        }
        return transferDTOList;
    }

    @Override
    public List<TransferDTO> findByDate(Date dateMin, Date dateMax, Integer page, Integer limit) {

        List<TransferEntity> transferEntityList;
        if (page != null && limit != null) {
            if (dateMin != null && dateMax == null) {
                Page<TransferEntity> transferEntityPage = transferRepository.findAllByCreateAtGreaterThanEqualAndCreateAtLessThanEqualOrderByIdDesc(dateMin, dateMin, PageRequest.of(page, limit));
                transferEntityList = transferEntityPage.toList();
            } else {
                Page<TransferEntity> transferEntityPage = transferRepository.findAllByCreateAtGreaterThanEqualAndCreateAtLessThanEqualOrderByIdDesc(dateMin, dateMax, PageRequest.of(page, limit));
                transferEntityList = transferEntityPage.toList();
            }

        } else {
            transferEntityList = transferRepository.findAllByCreateAtGreaterThanEqualAndCreateAtLessThanEqualOrderByIdDesc(dateMin, dateMax);
        }
        List<TransferDTO> transferDTOList = new ArrayList<>();

        for (TransferEntity transferEntity : transferEntityList) {
            TransferDTO transferDTO = new TransferDTO(transferEntity);

            transferDTOList.add(transferDTO);
        }
        return transferDTOList;
    }

    //the history of inventory by id inventory
    @Override
    public List<TransferDTO> findHistory(int id, int page, int limit) {
        try {
            List<TransferDTO> transferDTOS = new ArrayList<>();
            int start = (page - 1) * limit;
            System.out.println(start);
            List<TransferEntity> transferEntities = transferRepository.findHistoryByInventoryId(id, start, limit);
            if (transferEntities.isEmpty()) {
                return null;
            }
            for (TransferEntity transferEntity :
                    transferEntities) {
                TransferDTO transferDTO = new TransferDTO(transferEntity);
                transferDTOS.add(transferDTO);
            }
            return transferDTOS;
        } catch (Exception e) {
            return null;
        }
    }

    //the history import inventory by inventory id
    @Override
    public List<TransferDTO> findByInventoryInputId(int id, int page, int limit) {
        try {
            int start = (page - 1) * limit;
            List<TransferEntity> transferEntities = transferRepository.findByInventoryInputId(id, start, limit);
            List<TransferDTO> transferDTOS = new ArrayList<>();
            if (transferEntities.isEmpty()) {
                return null;
            }
            for (TransferEntity transferEntity :
                    transferEntities) {
                TransferDTO transferDTO = new TransferDTO(transferEntity);
                transferDTOS.add(transferDTO);
            }
            return transferDTOS;
        } catch (Exception e) {
            return null;
        }
    }

    //the history export inventory by inventory id
    @Override
    public List<TransferDTO> findByInventoryOutputId(int id, int page, int limit) {
        try {
            int start = (page - 1) * limit;
            List<TransferEntity> transferEntities = transferRepository.findByInventoryOutputId(id, start, limit);
            List<TransferDTO> transferDTOS = new ArrayList<>();
            if (transferEntities.isEmpty()) {
                return null;
            }
            for (TransferEntity transferEntity :
                    transferEntities) {
                TransferDTO transferDTO = new TransferDTO(transferEntity);
                transferDTOS.add(transferDTO);
            }
            return transferDTOS;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<TransferDTO> findingByKeyForHistory(String key) {
        return null;
    }

    @Override
    public long sizeHistory(int id) {
        long size = 0;
        try {
            size = transferRepository.sizeHistory(id);
        } catch (Exception e) {
            size = 0;
        }
        return size;
    }

    @Override
    public long sizeExport(int id) {
        long size = 0;
        try {
            size = transferRepository.sizeExport(id);
        } catch (Exception e) {
            size = 0;
        }
        return size;
    }

    @Override
    public long sizeImport(int id) {
        long size = 0;
        try {
            size = transferRepository.sizeImport(id);
        } catch (Exception e) {
            size = 0;
        }
        return size;
    }
    //hugnnq


    @Override
    public long sizeFindingBetween(String from, String to, int id) {
        long size = 0;
        try {
            size = transferRepository.countFindingBetWeen(from, to, id);
        } catch (Exception e) {
            size = 0;
        }
        System.out.println(size);
        return size;
    }


    @Override
    public long sizeSearch(String key, int id) {
        long size = 0;
        try {
            size = transferRepository.sizeSearch(key, id);

        } catch (Exception e) {
            size = 0;
        }
        return size;
    }


    @Override
    public List<HistoryTransferDTO> historyTransfer(int transferId, Integer page, Integer limit) {
        List<HistoryEntity> historyEntityList;
        if (page != null && limit != null) {
            Page<HistoryEntity> historyEntityPage = historyRepository.findAllByTransferId(transferId, PageRequest.of(page, limit));
            historyEntityList = historyEntityPage.toList();

        } else {
            historyEntityList = historyRepository.findAllByTransferId(transferId);
        }
        List<HistoryTransferDTO> historyTransferDTOList = new ArrayList<>();

        for (HistoryEntity historyEntity : historyEntityList) {
            HistoryTransferDTO historyTransferDTO = new HistoryTransferDTO(historyEntity);

            historyTransferDTOList.add(historyTransferDTO);
        }
        return historyTransferDTOList;
    }

    @Override
    public long countAll() {
        return transferRepository.count();
    }

    //tìm phiếu chuyển theo inventory_id, date, key
    @Override
    public List<TransferDTO> findAllByInventoryAndDate(String key, Date date, int inventory, PaginationDto page) {
        List<TransferEntity> listEntity = transferRepository.findAllByInventoryAndDate(date, inventory);
        page.setCount(listEntity.size());
        List<TransferDTO> list = new ArrayList<>();
        if(listEntity.size()==0 || listEntity == null){
            return null;
        }
        if (key != "" || key != null) {
            listEntity = listEntity.stream().filter(item -> {
                if(item.getCode().contains(key) || item.getInventoryInput().getName().contains(key)
                        ||item.getInventoryOutput().getName().contains(key) || item.getUser().getUsername().contains(key)){
                    return true;
                }else {
                    return false;
                }
            }).collect(Collectors.toList());
        }
        Integer offset = page.getCurrentPage()-1;
        Integer limit = page.getLimit();
        limit = limit>listEntity.size()?listEntity.size():limit;

        if (offset != null && limit != null) {
            int start = offset * limit;
            limit = (limit> listEntity.size()?listEntity.size(): limit);
            listEntity = listEntity.subList(start, start + limit);
        }
        listEntity.forEach(item -> list.add(new TransferDTO(item)));

        return list;
    }
}
