package com.sapo.edu.managetransferslip.service.impl;

import com.sapo.edu.managetransferslip.model.dto.product.ProductForInventoryDTO;
import com.sapo.edu.managetransferslip.model.dto.product.ProductsDTO;
import com.sapo.edu.managetransferslip.model.dto.product.UpdateProductDTO;
import com.sapo.edu.managetransferslip.model.entity.CategoriesEntity;
import com.sapo.edu.managetransferslip.model.entity.InventoriesEntity;
import com.sapo.edu.managetransferslip.model.entity.InventoryDetailEntity;
import com.sapo.edu.managetransferslip.model.entity.ProductsEntity;
import com.sapo.edu.managetransferslip.repository.CategoryRepository;
import com.sapo.edu.managetransferslip.repository.InventoryRepository;
import com.sapo.edu.managetransferslip.repository.ProductForInventoryRepository;
import com.sapo.edu.managetransferslip.repository.ProductRepository;
import com.sapo.edu.managetransferslip.service.ProductService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

@Service
public class ProductServiceImpl implements ProductService<ProductsDTO> {

    private final ProductRepository PRODUCT_REPOSITORY;
    private final ProductForInventoryRepository PRODUCT_FOR_INVENTORY_REPOSITORY;
    private final CategoryRepository CATEGORY_REPOSITORY;
    private final InventoryRepository INVENTORY_REPOSITORY;

    private Timestamp currentTime = new Timestamp(System.currentTimeMillis());

    public ProductServiceImpl(ProductRepository product_repository, ProductForInventoryRepository product_for_inventory_respository, CategoryRepository category_repository, InventoryRepository inventory_repository) {
        PRODUCT_REPOSITORY = product_repository;
        PRODUCT_FOR_INVENTORY_REPOSITORY = product_for_inventory_respository;
        CATEGORY_REPOSITORY = category_repository;
        INVENTORY_REPOSITORY = inventory_repository;
    }

    @Override
    public List<ProductsDTO> getAll(Pageable pageable) {

        List<ProductsEntity> productsEntities = PRODUCT_REPOSITORY.findProductWithoutDelete(pageable);
        List<ProductsDTO> productsDTOS = new LinkedList<>();

        for (ProductsEntity productsEntity : productsEntities) {

            productsDTOS.add(new ProductsDTO(productsEntity));

        }
        return productsDTOS;
    }

    @Override
    public List<ProductsDTO> findAll(Pageable pageable) {
        List<ProductsDTO> productsDTOS = new LinkedList<>();

        Page<ProductsEntity> productsEntities = PRODUCT_REPOSITORY.findAll(pageable);
        for (ProductsEntity productsEntity : productsEntities) {
            productsDTOS.add(new ProductsDTO(productsEntity));
        }
        return productsDTOS;
    }

    @Override
    public ProductsDTO create(ProductForInventoryDTO productForInventoryDTO) {
        CategoriesEntity categoriesEntity = CATEGORY_REPOSITORY.findByName(productForInventoryDTO.getCategoryName());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        ProductsEntity productsEntity = modelMapper.map(productForInventoryDTO, ProductsEntity.class);
        productsEntity.setCategory(categoriesEntity);

        PRODUCT_REPOSITORY.save(productsEntity);
        return new ProductsDTO(productsEntity);
    }


    @Transactional
    public List<String> createInventoryDetail(ProductForInventoryDTO productForInventoryDTO) {
        List<String> messages = new LinkedList<>();
        boolean check = true;
        if (productForInventoryDTO.getCode() == null) {
            productForInventoryDTO.setCode("pro" + (PRODUCT_REPOSITORY.getLastProductEntity().getId()+1));
            messages.add("0");

        } else {
            if (productForInventoryDTO.getCode() == "") {
                productForInventoryDTO.setCode("pro" + (PRODUCT_REPOSITORY.getLastProductEntity().getId()+1));
            } else if (PRODUCT_REPOSITORY.existsByCode(productForInventoryDTO.getCode())) {
                messages.add("1"); //code exit
                check = false;
            }
        }
        if (productForInventoryDTO.getName() == null) {
            System.out.println("name null");
            messages.add("2"); // name is null
            check = false;
        }
        if (productForInventoryDTO.getPrice() == null) {
            System.out.println("price null");
            messages.add("3"); // price is null
            check = false;
        }
        if (productForInventoryDTO.getCategoryName() == null) {
            System.out.println("category null");
            messages.add("4"); // category is null
            check = false;
        }
        if (productForInventoryDTO.getColor() == null) {
            System.out.println("color null");
            messages.add("5"); // color is null
            check = false;
        }
        if (productForInventoryDTO.getSize() == null) {
            System.out.println("size null");
            messages.add("6"); // size is null
            check = false;
        }
        if(productForInventoryDTO.getLink()!=null){

        }


        if (check == true) {
            List<InventoriesEntity> inventoriesEntities = INVENTORY_REPOSITORY.getAll();

            create(productForInventoryDTO);
            for(InventoriesEntity inventoriesEntity:inventoriesEntities){
                InventoryDetailEntity inventoryDetailEntity = new InventoryDetailEntity();
                ProductsEntity productsEntity = getLastProductEntity();
                System.out.println("inventory: "+inventoriesEntity);
                inventoryDetailEntity.setProduct(productsEntity);
                inventoryDetailEntity.setInventory(inventoriesEntity);
                PRODUCT_FOR_INVENTORY_REPOSITORY.save(inventoryDetailEntity);
            }
        }

        return messages;
    }

    public ProductsEntity getLastProductEntity() {
        return PRODUCT_REPOSITORY.getLastProductEntity();
    }

    @Override
    public int getLastProduct() {
        return PRODUCT_REPOSITORY.getLastProduct();
    }
    @Override
    public int getLastInventoryDetail() {
        return PRODUCT_FOR_INVENTORY_REPOSITORY.getLastInventoryDetail();
    }
    @Override
    public ProductsDTO update(UpdateProductDTO updateProductDTO, int id) {


        List<ProductsEntity> productsEntities = PRODUCT_REPOSITORY.findAll();
        ProductsEntity productsEntity = PRODUCT_REPOSITORY.getById(id);
        if (updateProductDTO.getCode() != null) {
            productsEntity.setCode(updateProductDTO.getCode());
        }

        if (updateProductDTO.getName() != null && updateProductDTO.getName() != "") {
            productsEntity.setName(updateProductDTO.getName());
        }
        if (updateProductDTO.getDescription() != null) {
            productsEntity.setDescription(updateProductDTO.getDescription());
        }
        if (updateProductDTO.getCategory_id() != 0) {
            CategoriesEntity categoryEntity = CATEGORY_REPOSITORY.getById(updateProductDTO.getCategory_id());
            productsEntity.setCategory(categoryEntity);
        }
        if (updateProductDTO.getSize() != null) {
            productsEntity.setSize(updateProductDTO.getSize());
        }
        if (updateProductDTO.getColor() != null) {
            productsEntity.setColor(updateProductDTO.getColor());
        }
        if (updateProductDTO.getPrice() != null) {
            productsEntity.setPrice(updateProductDTO.getPrice());
        }
        if (updateProductDTO.getLink() != null) {
            productsEntity.setLink(updateProductDTO.getLink());
        }

        PRODUCT_REPOSITORY.save(productsEntity);

        return new ProductsDTO(productsEntity);
    }

    @Override

    public ProductsDTO delete(int id) {
        InventoryDetailEntity inventoryDetailEntity = PRODUCT_FOR_INVENTORY_REPOSITORY.getById(id);
        inventoryDetailEntity.setDeletedAt(currentTime);
        PRODUCT_FOR_INVENTORY_REPOSITORY.save(inventoryDetailEntity);
        return null;
    }

    @Override
    public void undo(int id) {
        ProductsEntity productsEntity = PRODUCT_REPOSITORY.getById(id);
        productsEntity.setDeletedAt(null);
        PRODUCT_REPOSITORY.save(productsEntity);
    }

    @Override
    public List<ProductsDTO> search(String keySearch, Pageable pageable) {
        List<ProductsDTO> productsDTOS = new LinkedList<>();

        List<ProductsEntity> productsEntities = PRODUCT_REPOSITORY.searchProductByKey(keySearch, pageable);
        for (ProductsEntity productsEntity : productsEntities) {
            productsDTOS.add(new ProductsDTO(productsEntity));
        }
        return productsDTOS;
    }

    @Override
    public List<ProductsDTO> filterBySize(String keySearch, Pageable pageable) {

        List<ProductsDTO> productsDTOS = new LinkedList<>();

        List<ProductsEntity> productsEntities = PRODUCT_REPOSITORY.filterBySize(keySearch, pageable);
        for (ProductsEntity productsEntity : productsEntities) {
            productsDTOS.add(new ProductsDTO(productsEntity));
        }
        return productsDTOS;
    }

    @Override
    public List<ProductsDTO> filterByPrice(Double minPrice, Double maxPrice, Pageable pageable) {
        List<ProductsDTO> productsDTOS = new LinkedList<>();

        List<ProductsEntity> productsEntities = PRODUCT_REPOSITORY.filterByPrice(minPrice, maxPrice, pageable);

        for (ProductsEntity productsEntity : productsEntities) {
            productsDTOS.add(new ProductsDTO(productsEntity));
        }
        return productsDTOS;
    }

    @Override
    public List<ProductsDTO> filterByCategory(String keySearch, Pageable pageable) {
        List<ProductsDTO> productsDTOS = new LinkedList<>();

        List<ProductsEntity> productsEntities = PRODUCT_REPOSITORY.filterByCategory(keySearch, pageable);
        for (ProductsEntity productsEntity : productsEntities) {
            productsDTOS.add(new ProductsDTO(productsEntity));
        }
        return productsDTOS;
    }

    @Override
    public List<ProductsDTO> filterByColor(String keySearch, Pageable pageable) {
        List<ProductsDTO> productsDTOS = new LinkedList<>();

        List<ProductsEntity> productsEntities = PRODUCT_REPOSITORY.filterByColor(keySearch, pageable);
        for (ProductsEntity productsEntity : productsEntities) {
            productsDTOS.add(new ProductsDTO(productsEntity));
        }

        return productsDTOS;
    }

    @Override
    public List<ProductsDTO> sortAscByName(Pageable pageable) {
        List<ProductsDTO> productsDTOS = new LinkedList<>();

        List<ProductsEntity> productsEntities = PRODUCT_REPOSITORY.sortAscByName(pageable);
        for (ProductsEntity productsEntity : productsEntities) {
            productsDTOS.add(new ProductsDTO(productsEntity));
        }
        return productsDTOS;
    }

    public List<ProductsDTO> sortDescByName(Pageable pageable) {
        List<ProductsDTO> productsDTOS = new LinkedList<>();

        List<ProductsEntity> productsEntities = PRODUCT_REPOSITORY.sortDescByName(pageable);
        for (ProductsEntity productsEntity : productsEntities) {
            productsDTOS.add(new ProductsDTO(productsEntity));
        }
        return productsDTOS;
    }

    @Override
    public List<ProductsDTO> sortAscByPrice(Pageable pageable) {
        List<ProductsDTO> productsDTOS = new LinkedList<>();

        List<ProductsEntity> productsEntities = PRODUCT_REPOSITORY.sortAscByPrice(pageable);
        for (ProductsEntity productsEntity : productsEntities) {
            productsDTOS.add(new ProductsDTO(productsEntity));
        }
        return productsDTOS;
    }

    @Override
    public List<ProductsDTO> sortDescByPrice(Pageable pageable) {
        List<ProductsDTO> productsDTOS = new LinkedList<>();

        List<ProductsEntity> productsEntities = PRODUCT_REPOSITORY.sortDescByPrice(pageable);
        for (ProductsEntity productsEntity : productsEntities) {
            productsDTOS.add(new ProductsDTO(productsEntity));
        }
        return productsDTOS;
    }

    @Override
    public List<ProductsDTO> sortAscByNumber(Pageable pageable) {
        List<ProductsDTO> productsDTOS = new LinkedList<>();

        List<ProductsEntity> productsEntities = PRODUCT_REPOSITORY.sortAscByNumber(pageable);
        for (ProductsEntity productsEntity : productsEntities) {
            productsDTOS.add(new ProductsDTO(productsEntity));
        }
        return productsDTOS;
    }

    @Override
    public List<ProductsDTO> sortDescByNumber(Pageable pageable) {
        List<ProductsDTO> productsDTOS = new LinkedList<>();

        List<ProductsEntity> productsEntities = PRODUCT_REPOSITORY.sortDescByNumber(pageable);
        for (ProductsEntity productsEntity : productsEntities) {
            productsDTOS.add(new ProductsDTO(productsEntity));
        }
        return productsDTOS;
    }

    @Override
    public List<ProductForInventoryDTO> getProductForInventory(int inventoryId, Integer page, Integer size) {
        List<ProductForInventoryDTO> productForInventoryDTOS = new LinkedList<>();
        List<InventoryDetailEntity> inventoryDetailEntities = new LinkedList<>();
        if (page != null && size != null) {
            inventoryDetailEntities = PRODUCT_FOR_INVENTORY_REPOSITORY.findProductWithoutDelete(inventoryId, PageRequest.of(page, size));
        } else {
            inventoryDetailEntities = PRODUCT_FOR_INVENTORY_REPOSITORY.findProductWithoutDelete0(inventoryId);
//            inventoryDetailEntities = PRODUCT_FOR_INVENTORY_REPOSITORY.findAll();
        }
        for (InventoryDetailEntity inventoryDetailEntity : inventoryDetailEntities) {
            productForInventoryDTOS.add(new ProductForInventoryDTO(inventoryDetailEntity));

        }
        return productForInventoryDTOS;
    }

    @Override
    public List<ProductForInventoryDTO> searchProductByInventory(int inventory, String keySearch, Integer page, Integer size) {
        List<ProductForInventoryDTO> productForInventoryDTOS = new LinkedList<>();
        List<InventoryDetailEntity> inventoriesEntities = new LinkedList<>();

        if (keySearch == null) {
            if (page != null && size != null) {
                inventoriesEntities = PRODUCT_FOR_INVENTORY_REPOSITORY.findProductWithoutDelete(inventory, PageRequest.of(page, size));
            } else {
                inventoriesEntities = PRODUCT_FOR_INVENTORY_REPOSITORY.findProductWithoutDelete0(inventory);
            }
        } else {
            if (page != null && size != null) {
                inventoriesEntities = PRODUCT_FOR_INVENTORY_REPOSITORY.searchProductByInventory(inventory, keySearch, PageRequest.of(page, size));
            } else {
                inventoriesEntities = PRODUCT_FOR_INVENTORY_REPOSITORY.searchProductByInventory(inventory, keySearch);
            }
        }

        for (InventoryDetailEntity inventoryDetailEntity : inventoriesEntities) {
            productForInventoryDTOS.add(new ProductForInventoryDTO(inventoryDetailEntity));
        }
        return productForInventoryDTOS;
    }

    @Override
    public String updateProductForInventory(ProductForInventoryDTO productForInventoryDTO, int id) {

        if (PRODUCT_REPOSITORY.existsByCode(productForInventoryDTO.getCode())) {
            InventoryDetailEntity inventoryDetailEntity = PRODUCT_FOR_INVENTORY_REPOSITORY.getById(id);
            ProductsEntity productsEntity = PRODUCT_REPOSITORY.getById(id);
            if (productForInventoryDTO.getCode() != null) {
                productsEntity.setCode(productForInventoryDTO.getCode());
            }

            if (productForInventoryDTO.getName() != null) {
                productsEntity.setName(productForInventoryDTO.getName());
            }
            if (productForInventoryDTO.getDescription() != null) {
                productsEntity.setDescription(productForInventoryDTO.getDescription());
            }

            if (productForInventoryDTO.getSize() != null) {
                productsEntity.setSize(productForInventoryDTO.getSize());
            }
            if (productForInventoryDTO.getColor() != null) {
                productsEntity.setColor(productForInventoryDTO.getColor());
            }
            if (productForInventoryDTO.getPrice() != null) {
                productsEntity.setPrice(productForInventoryDTO.getPrice());
            }
            if (productForInventoryDTO.getLink() != null) {
                productsEntity.setLink(productForInventoryDTO.getLink());
            }

            if (productForInventoryDTO.getNumberPro() != 0) {
                inventoryDetailEntity.setNumberPro(productForInventoryDTO.getNumberPro());
            }
            if (productForInventoryDTO.getCategoryName() != null) {
                try {
                    productsEntity.setCategory(CATEGORY_REPOSITORY.findByName(productForInventoryDTO.getCategoryName()));
                } catch (Exception e) {
                    System.out.println("error in category");
                }
            }

//            PRODUCT_FOR_INVENTORY_REPOSITORY.save(inventoryDetailEntity);
//            PRODUCT_REPOSITORY.save(productsEntity);

            return "123";
        } else {
            return "0";
        }
    }

    @Override
    public Stack showData(String keySearch, int inventory, Integer page, Integer size) {
        Stack data = new Stack();
        if (keySearch == null) {
            data.push(getProductForInventory(inventory, page, size));
            data.push(PRODUCT_FOR_INVENTORY_REPOSITORY.getTotalRowInventoryDetail(inventory));
        } else {
            data.push(searchProductByInventory(inventory, keySearch, page, size));
            data.push(PRODUCT_FOR_INVENTORY_REPOSITORY.getTotalRowSearchInventoryDetail(inventory, keySearch));
        }
        return data;
    }

    @Override
    public ProductForInventoryDTO getProductForInventoryById(int idInventory, int idProduct) {
        InventoryDetailEntity inventoryDetailEntity = PRODUCT_FOR_INVENTORY_REPOSITORY.ProductForInventoryDTO(idInventory, idProduct);
        return new ProductForInventoryDTO(inventoryDetailEntity);
    }

    @Override
    public int getTotalProductInInventory(int inventoryId){
        return PRODUCT_FOR_INVENTORY_REPOSITORY.getTotalProductInInventory(inventoryId);
    }
}
