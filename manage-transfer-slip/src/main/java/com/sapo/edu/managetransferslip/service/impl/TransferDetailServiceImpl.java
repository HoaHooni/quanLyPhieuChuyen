package com.sapo.edu.managetransferslip.service.impl;

import com.sapo.edu.managetransferslip.model.dto.Message;
import com.sapo.edu.managetransferslip.model.entity.*;
import com.sapo.edu.managetransferslip.repository.*;
import com.sapo.edu.managetransferslip.service.TransferDetailService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransferDetailServiceImpl implements TransferDetailService {
    private final TransferDetailRepository transferDetailRepository;
    private final TransferRepository transferRepository;
    private  final InventoryRepository inventoryRepository;
    private final InventoryDetailRepository inventoryDetailRepository;
    private final ProductRepository productRepository;

    public TransferDetailServiceImpl(TransferDetailRepository transferDetailRepository, TransferRepository transferRepository, InventoryRepository inventoryRepository, InventoryDetailRepository inventoryDetailRepository, ProductRepository productRepository) {
        this.transferDetailRepository = transferDetailRepository;
        this.transferRepository = transferRepository;
        this.inventoryRepository = inventoryRepository;
        this.inventoryDetailRepository = inventoryDetailRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Message deleteTransferDetail(Integer id) {

        try {
            TransferDetailEntity transferDetailEntity = transferDetailRepository.getById(id);
            if(transferDetailEntity !=null){
                TransferEntity transferEntity = transferRepository.getById(transferDetailEntity.getTransfer().getId());
                InventoriesEntity inventoriesEntity_input = inventoryRepository.getById(transferEntity.getInventoryInput().getId());
                InventoriesEntity inventoriesEntity_output = inventoryRepository.getById(transferEntity.getInventoryOutput().getId());
                ProductsEntity productsEntity = productRepository.getById(transferDetailEntity.getProduct().getId());

                InventoryDetailEntity inventoryDetailEntity_input = inventoryDetailRepository.findWithElementId(productsEntity.getId(),inventoriesEntity_input.getId());
                InventoryDetailEntity inventoryDetailEntity_output = inventoryDetailRepository.findWithElementId(productsEntity.getId(),inventoriesEntity_output.getId());

                inventoryDetailEntity_input.setNumberPro(inventoryDetailEntity_input.getNumberPro() - transferDetailEntity.getTotal());
                inventoryDetailEntity_output.setNumberPro(inventoryDetailEntity_output.getNumberPro() + transferDetailEntity.getTotal());

                transferDetailRepository.delete(transferDetailEntity);

                return new Message("Delete successfully, transferDetailId: " + id);
            }else{
                return new Message("transferDetailId: " + id+" is not found.");
            }
        } catch (Exception e) {
            return new Message("Delete failed.");
        }

    }
}
