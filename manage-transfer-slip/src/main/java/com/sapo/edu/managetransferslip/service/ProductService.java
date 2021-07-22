package com.sapo.edu.managetransferslip.service;

import com.sapo.edu.managetransferslip.model.dto.product.ProductForInventoryDTO;
import com.sapo.edu.managetransferslip.model.dto.product.UpdateProductDTO;
import com.sapo.edu.managetransferslip.model.entity.InventoryDetailEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService<T> {


    List<T> getAll(Pageable pageable);

    List<T> findAll(Pageable pageable);

    T create(ProductForInventoryDTO productForInventoryDTO);

    List<String> createInventoryDetail(ProductForInventoryDTO productForInventoryDTO);

    T update(UpdateProductDTO updateProductDTO, int id);

    T delete(int id);

    void undo(int id);

    List<T> search(String keySearch, Pageable pageable);

    List<T> filterBySize(String keySearch, Pageable pageable);

    List<T> filterByPrice(Double minPrice, Double maxDouble, Pageable pageable);

    List<T> filterByCategory(String keySearch, Pageable pageable);

    List<T> filterByColor(String keySearch, Pageable pageable);

    List<T> sortAscByName(Pageable pageable);

    List<T> sortDescByName(Pageable pageable);

    List<T> sortAscByPrice(Pageable pageable);

    List<T> sortDescByPrice(Pageable pageable);

    List<T> sortAscByNumber(Pageable pageable);

    List<T> sortDescByNumber(Pageable pageable);

    List<ProductForInventoryDTO> getProductForInventory(int inventoryId, Integer page, Integer size);

    List<ProductForInventoryDTO> searchProductByInventory(int inventory, String keySearch, Integer page, Integer size);

    String updateProductForInventory(ProductForInventoryDTO productForInventoryDTO, int id);


    List<ProductForInventoryDTO> showData(String keySearch, int inventory, Integer page, Integer size);

    ProductForInventoryDTO getProductForInventoryById(int idInventory, int idProduct);

    int getLastProduct();
    int getLastInventoryDetail();
    int getTotalProductInInventory(int inventoryId);
}