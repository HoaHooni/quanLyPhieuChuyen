package com.sapo.edu.managetransferslip.service.impl;

import com.sapo.edu.managetransferslip.model.dto.CustomInventoriesDetails;
import com.sapo.edu.managetransferslip.model.dto.InventoriesDTO;
import com.sapo.edu.managetransferslip.model.dto.InventoryDetailDTO;
import com.sapo.edu.managetransferslip.model.dto.InventoryDetailReportDTO;
import com.sapo.edu.managetransferslip.model.dto.product.ProductsDTO;
import com.sapo.edu.managetransferslip.model.entity.InventoriesEntity;
import com.sapo.edu.managetransferslip.model.entity.InventoryDetailEntity;
import com.sapo.edu.managetransferslip.model.entity.ProductsEntity;
import com.sapo.edu.managetransferslip.repository.InventoriesRepository;
import com.sapo.edu.managetransferslip.repository.InventoryDetailRepository;
import com.sapo.edu.managetransferslip.repository.InventoryRepository;
import com.sapo.edu.managetransferslip.repository.ProductRepository;
import com.sapo.edu.managetransferslip.service.InventoryDetailService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class InventoryDetailImpl implements InventoryDetailService {

    private final InventoryDetailRepository inventoryDetailRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public InventoryDetailImpl(InventoryDetailRepository inventoryDetailRepository, InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryDetailRepository = inventoryDetailRepository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    //add
    @Override
    public ResponseEntity<InventoryDetailDTO> add(InventoryDetailDTO inventoryDetailDTO) {
        boolean check = already(inventoryDetailDTO);
        System.out.println(check);
        if (check == false) {
            try {
                inventoryDetailRepository.save(convertToEntity(inventoryDetailDTO));

                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            }
        } else {
            System.out.println(inventoryDetailDTO.getProduct_id());
            System.out.println(inventoryDetailDTO.getNumberPro());
            return updateNumberProduct(inventoryDetailDTO.getProduct_id(), inventoryDetailDTO.getNumberPro());
        }

    }


    @Override
    public ResponseEntity<InventoryDetailDTO> deleteByProductId(int id) {
        if (alreadyProductId(id) == true) {
            if (getNumberProducts(id) == 0) {
                try {
                    inventoryDetailRepository.deleteByProductId(id);
                } catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
                }
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @Override
    public ResponseEntity<InventoryDetailDTO> deleteByInventoryId(int id) {
        System.out.println(alreadyInventoryId(id));
        if (alreadyInventoryId(id) == true) {
            try {
                List<InventoryDetailEntity> inventoryDetailEntities = inventoryDetailRepository.findAllProductsInInventory(id);
                for (InventoryDetailEntity inventoryDetailEntity :
                        inventoryDetailEntities) {
                    if (inventoryDetailEntity.getNumberPro() != 0) {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                }
                inventoryDetailRepository.deleteByInventoryId(id);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<InventoryDetailDTO> update(int id, int numberProduct) {
        if (alreadyProductId(id) == true) {
            InventoryDetailEntity inventoryDetailEntity = inventoryDetailRepository.findByProductId(id);
            inventoryDetailEntity.setNumberPro(numberProduct);
            try {
                inventoryDetailRepository.save(inventoryDetailEntity);

            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public List<InventoryDetailDTO> getData() {
        List<InventoryDetailEntity> data = inventoryDetailRepository.findAll();
        List<InventoryDetailDTO> dataRespone = new ArrayList<>();
        for (InventoryDetailEntity inventoryDetailEntity : data) {
            dataRespone.add(convertToDTO(inventoryDetailEntity));
        }
        return dataRespone;
    }

    public ResponseEntity<InventoryDetailDTO> updateNumberProduct(int id, int numberProduct) {
        InventoryDetailEntity inventoryDetailEntity = inventoryDetailRepository.findByProductId(id);
        if (inventoryDetailEntity == null) {
            System.out.println("null roi nha00");
        }
        inventoryDetailEntity.setNumberPro(inventoryDetailEntity.getNumberPro() + numberProduct);
        try {
            inventoryDetailRepository.save(inventoryDetailEntity);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    public int getNumberProducts(int id) {
        if (alreadyProductId(id) == true) {
            try {
                return inventoryDetailRepository.findByProductId(id).getNumberPro();

            } catch (Exception e) {
                return 0;
            }

        }
        return 0;
    }


    @Override
    public boolean already(InventoryDetailDTO inventoryDetailDTO) {
        InventoryDetailEntity inventoryDetailEntity = inventoryDetailRepository.findWithElementId(inventoryDetailDTO.getProduct_id(), inventoryDetailDTO.getInventory_id());
        System.out.println(inventoryDetailEntity);
        if (inventoryDetailEntity == null) {
            return false;
        }
        return true;

    }

    @Override
    public long countProduct() {
        long count = 0;
        try {
            count = inventoryDetailRepository.count();
        } catch (Exception e) {
            System.out.println("not found");
            count = 0;
        }
        return count;
    }

    @Override
    public long sumNumberProduct() {
        long sum = 0;
        try {
            sum = inventoryDetailRepository.sum();
        } catch (Exception e) {
            sum = 0;
        }
        return sum;
    }

    @Override
    public long countByInventoryId(int id) {
        long count = 0;
        try {
            count = inventoryDetailRepository.countProductOfInventory(id);
        } catch (Exception e) {
            System.out.println("id not found");

        }
        return count;
    }

    @Override
    public long sumByInventoryId(int id) {
        long sum = 0;
        try{
            sum = inventoryDetailRepository.sumProductOfInventory(id);
        }catch (Exception e){
            sum = 0;
            System.out.println("not found");
        }
        return sum;
    }

    @Override
    public long sumProductById(int id) {
        long sum = 0;
        try{
            sum = inventoryDetailRepository.sumProductById(id);
        }catch (Exception e){
            sum = 0;
            System.out.println("not found");
            sum = 0 ;
        }
        return sum;
    }

    @Override
    public long sizeProduct(int id) {
      try{
          InventoriesEntity inventoriesEntity = inventoryRepository.findById(id).get();
          if (inventoriesEntity != null){

              System.out.println(inventoriesEntity.getId());
              List<InventoryDetailEntity> inventoryDetailEntities = inventoryDetailRepository.findAllProductsInInventory(inventoriesEntity.getId());
              if (inventoryDetailEntities.isEmpty())
              {
                  return 0;
              }
              System.out.println("size: " + inventoryDetailEntities.size());
              List<ProductsDTO> pages = new ArrayList<>();
              for (InventoryDetailEntity inventoryDetailEntity :
                      inventoryDetailEntities) {
                  System.out.println("product_id: " + inventoryDetailEntity.getProduct().getId());
                  ProductsEntity productsEntity = productRepository.findById(inventoryDetailEntity.getProduct().getId()).get();
                  if (productsEntity != null)
                  {
                      pages.add(new ProductsDTO(productsEntity));
                  }
              }
              System.out.println( "size page: " + pages.size());
              return pages.size();
          }
          return 0;
      }catch (Exception e){
          return 0;
      }
    }

    @Override
    public InventoryDetailReportDTO detailsById(int id, int page, int limit) {
        InventoriesEntity inventoriesEntity = inventoryRepository.findById(id).get();
        if (inventoriesEntity != null)
        {
            int start = (page - 1) * limit;
            List<InventoryDetailEntity> inventoryDetailEntities = inventoryDetailRepository.findByInventoryId(id,start,limit);
            if (inventoryDetailEntities.isEmpty()){
                return null;
            }
            List<CustomInventoriesDetails> customInventoriesDetails = new ArrayList<>();
            for (InventoryDetailEntity inventoryDetailEntity:
                    inventoryDetailEntities) {
                ProductsEntity productsEntity = productRepository.findById(id).get();
                if (productsEntity != null){
                    System.out.println(productsEntity);
                    System.out.println("number: " + inventoryDetailEntity.getNumberPro());
                    customInventoriesDetails.add(new CustomInventoriesDetails(new ProductsDTO( inventoryDetailEntity.getProduct()),inventoryDetailEntity.getNumberPro(),inventoryDetailEntity.getId()));
                }
            }
            int sumNumberProducts = inventoryDetailRepository.sumProductOfInventory(id);
            System.out.println(sumNumberProducts);

            return new InventoryDetailReportDTO( new InventoriesDTO(inventoriesEntity),customInventoriesDetails);
        }
        return null;

    }

    @Override
    public InventoryDetailReportDTO findWithKey(int id,String key,int page,int limit) {
        InventoriesEntity inventoriesEntity = inventoryRepository.findById(id).get();
        int start = (page - 1) * limit;
        if (inventoriesEntity != null){
            List<ProductsEntity> productsEntities = productRepository.findByKey(key,start,limit);
            if (productsEntities.isEmpty())
            {
                return null;
            }
            List<CustomInventoriesDetails>  customInventoriesDetails = new ArrayList<>();
            int sum = 0;
            for (ProductsEntity productsEntity :
                    productsEntities) {
                InventoryDetailEntity inventoryDetailEntity = inventoryDetailRepository.findWithElementId(productsEntity.getId(),inventoriesEntity.getId());

               if (inventoryDetailEntity != null)
               {
                   //(productDto,number,id)
                   sum += inventoryDetailEntity.getNumberPro();
                    customInventoriesDetails.add(new CustomInventoriesDetails(new ProductsDTO(inventoryDetailEntity.getProduct()),inventoryDetailEntity.getNumberPro(),inventoryDetailEntity.getId()));
               }
            }
            return new InventoryDetailReportDTO(new InventoriesDTO(inventoriesEntity),customInventoriesDetails);

        }
        return  null;

    }

    @Override
    public long sizeFindByKey(int id, String key) {
        InventoriesEntity inventoriesEntity = inventoryRepository.findById(id).get();
        if (inventoriesEntity != null){
            List<ProductsEntity> productsEntities = productRepository.sizeFindByKey(key);
            if (productsEntities.isEmpty())
            {
                return 0;
            }
            List<CustomInventoriesDetails>  customInventoriesDetails = new ArrayList<>();
            int sum = 0;
            for (ProductsEntity productsEntity :
                    productsEntities) {
                InventoryDetailEntity inventoryDetailEntity = inventoryDetailRepository.findWithElementId(productsEntity.getId(),inventoriesEntity.getId());

                if (inventoryDetailEntity != null)
                {
                    //(productDto,number,id)
                    sum += inventoryDetailEntity.getNumberPro();
                    customInventoriesDetails.add(new CustomInventoriesDetails(new ProductsDTO(inventoryDetailEntity.getProduct()),inventoryDetailEntity.getNumberPro(),inventoryDetailEntity.getId()));
                }
            }
            return customInventoriesDetails.size();

        }
        return  0;

    }


    public boolean alreadyProductId(int id) {
        InventoryDetailEntity inventoryDetailEntity = inventoryDetailRepository.findByProductId(id);
        if (inventoryDetailEntity == null) {
            return false;
        }
        return true;
    }



    //already inventoryID
    public boolean alreadyInventoryId(int id) {
        List<InventoryDetailEntity> inventoryDetailEntities = inventoryDetailRepository.findAllProductsInInventory(id);
        if (inventoryDetailEntities.isEmpty()) {
            return false;
        }
        return true;
    }


    public InventoryDetailEntity convertToEntity(InventoryDetailDTO inventoryDetailDTO) {

        InventoryDetailEntity inventoryDetailEntity = new InventoryDetailEntity();
        inventoryDetailEntity.setId(inventoryDetailDTO.getId());
        inventoryDetailEntity.setInventory(inventoryRepository.findById(inventoryDetailDTO.getInventory_id()).get());
        inventoryDetailEntity.setProduct(productRepository.findById(inventoryDetailDTO.getProduct_id()).get());
        inventoryDetailEntity.setNumberPro(inventoryDetailDTO.getNumberPro());
        return inventoryDetailEntity;
    }

    public InventoryDetailDTO convertToDTO(InventoryDetailEntity inventoryDetailEntity) {
        InventoryDetailDTO inventoryDetailDTO = new InventoryDetailDTO();
        inventoryDetailDTO.setId(inventoryDetailEntity.getId());
        inventoryDetailDTO.setInventory_id(inventoryDetailEntity.getInventory().getId());
        inventoryDetailDTO.setProduct_id(inventoryDetailEntity.getProduct().getId());
        inventoryDetailDTO.setNumberPro(inventoryDetailEntity.getNumberPro());
        return inventoryDetailDTO;
    }


}
