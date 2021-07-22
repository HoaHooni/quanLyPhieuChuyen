package com.sapo.edu.managetransferslip.service.impl;

import com.sapo.edu.managetransferslip.dao.IReportDao;
import com.sapo.edu.managetransferslip.model.dto.*;
import com.sapo.edu.managetransferslip.model.dto.product.ProductsDTO;
import com.sapo.edu.managetransferslip.model.dto.statistic.*;
import com.sapo.edu.managetransferslip.repository.InventoriesRepository;
import com.sapo.edu.managetransferslip.repository.ProductRepository;
import com.sapo.edu.managetransferslip.repository.UserRepository;
import com.sapo.edu.managetransferslip.service.IReportService;
import lombok.SneakyThrows;

import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements IReportService {

    private IReportDao reportDao;

    private ProductRepository productRepository;

    private InventoriesRepository inventoriesRepository;

    private UserRepository userRepository;

    public ReportServiceImpl(IReportDao reportDao, ProductRepository productRepository, InventoriesRepository inventoriesRepository, UserRepository userRepository) {
        this.reportDao = reportDao;
        this.productRepository = productRepository;
        this.inventoriesRepository = inventoriesRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<StatisticProductTableDto> getSatisticProduct(StatisticProductFormDto statisticProductForm) {

        List<StatisticProductTableDto> list = reportDao.getSatisticProduct(statisticProductForm.getDateStart(), statisticProductForm.getDateEnd(), statisticProductForm.getInventory());

        if (list.size() == 0) return list;

        //Sort
        String sort = statisticProductForm.getSort().getColumn() == null ? "" : statisticProductForm.getSort().getColumn();
        boolean asc = statisticProductForm.getSort().isAsc();
        switch (sort) {
            case "code": {
                if (asc) {
                    Collections.sort(list, (o1, o2) -> o1.getCode().compareTo(o2.getCode()));
                } else {
                    Collections.sort(list, (o1, o2) -> o2.getCode().compareTo(o1.getCode()));
                }
                ;
                break;
            }
            case "name": {
                if (asc) {
                    Collections.sort(list, (o1, o2) -> o1.getName().compareTo(o2.getName()));
                } else {
                    Collections.sort(list, (o1, o2) -> o2.getName().compareTo(o1.getName()));
                }
                ;
                break;
            }
            case "transferImport": {
                if (asc) {
                    Collections.sort(list, (o1, o2) -> o1.getTransferImport() - (o2.getTransferImport()));
                } else {
                    Collections.sort(list, (o1, o2) -> o2.getTransferImport() - (o1.getTransferImport()));
                }
                ;
                break;
            }
            case "transferExport": {
                if (asc) {
                    Collections.sort(list, (o1, o2) -> o1.getTransferExport() - (o2.getTransferExport()));
                } else {
                    Collections.sort(list, (o1, o2) -> o2.getTransferExport() - (o1.getTransferExport()));
                }
                ;
                break;
            }
            case "productExport": {
                if (asc) {
                    Collections.sort(list, (o1, o2) -> (int) Math.ceil(o1.getProductExport() - (o2.getProductExport())));
                } else {
                    Collections.sort(list, (o1, o2) -> (int) Math.ceil(o2.getProductExport() - (o1.getProductExport())));
                }
                ;
                break;
            }
            case "productImport": {
                if (asc) {
                    Collections.sort(list, (o1, o2) -> (int) Math.ceil(o1.getProductImport() - (o2.getProductImport())));
                } else {
                    Collections.sort(list, (o1, o2) -> (int) Math.ceil(o2.getProductImport() - (o1.getProductImport())));
                }
                ;
                break;
            }

            default: {
                Collections.sort(list, (o1, o2) -> o1.getCode().compareTo(o2.getCode()));
                break;
            }

        }

        statisticProductForm.getPage().setCount(getCountPage(list));
        int page = statisticProductForm.getPage().getCurrentPage() == 0 ? 0 : statisticProductForm.getPage().getCurrentPage() - 1;
        int limit = statisticProductForm.getPage().getLimit();
        int start = limit * page, end = limit * (page + 1) <= getCountPage(list) ? limit * (page + 1) : getCountPage(list);

        list = list.subList(start, end);

        return list;
    }

    @Override
    public List<StatisticTransferTableDto> getStatisticTransfer(StatisticTransferFormDto statisticTransferFormDto) {

        try {
            List<StatisticTransferTableDto> list = reportDao.getStatisticTranser(statisticTransferFormDto.getDateStart(), statisticTransferFormDto.getDateEnd());

            if (statisticTransferFormDto.getInventoryInput() != 0) {
                InventoriesDTO inventoriesDTO = new InventoriesDTO(inventoriesRepository.getById(statisticTransferFormDto.getInventoryInput()));
                if (inventoriesDTO != null) {
                    list = list.stream().filter(a -> a.getInventoryInput().equals(inventoriesDTO.getName())).collect(Collectors.toList());
                } else {
                    throw new Exception(inventoriesDTO.getName() + " is not exist11111");
                }
            }

            if (statisticTransferFormDto.getInventoryOutput() != 0) {
                InventoriesDTO inventoriesDTO = new InventoriesDTO(inventoriesRepository.getById(statisticTransferFormDto.getInventoryOutput()));
                if (inventoriesDTO != null) {
                    list = list.stream().filter(a -> a.getInventoryOutput().equals(inventoriesDTO.getName())).collect(Collectors.toList());
                } else {
                    throw new Exception(inventoriesDTO.getName() + " is not exist");
                }
            }

            if (statisticTransferFormDto.getUser() != 0) {
                UsersDTO usersDTO = new UsersDTO(userRepository.getById(statisticTransferFormDto.getUser()));
                if (usersDTO != null) {
                    list = list.stream().filter(a -> a.getUsername().equals(usersDTO.getUsername())).collect(Collectors.toList());
                } else {
                    throw new Exception(usersDTO.getCode() + " is not exist");
                }
            }
            return list;
        } catch (Exception ex) {
            System.out.println("Error reportServiceImpl: " + ex);
//            throw new Exception(ex.getMessage());
            return null;
        }
    }

    @Override
    public List<StatisticDateTableDto> getStatisticDate(StatisticDateFormDto statisticDateFormDto) {

        List<StatisticDateTableDto> list = reportDao.getStatisticDate(statisticDateFormDto.getDateStart(), statisticDateFormDto.getDateEnd(), statisticDateFormDto.getInventory(), statisticDateFormDto.getUser());
        statisticDateFormDto.setDateEnd(statisticDateFormDto.getDateEnd() == null ? new Date(System.currentTimeMillis()) : statisticDateFormDto.getDateEnd());

        if (list.size() == 0) return list;

        //Sort
        String sort = statisticDateFormDto.getSort().getColumn() == null ? "" : statisticDateFormDto.getSort().getColumn();
        boolean asc = statisticDateFormDto.getSort().isAsc();
        switch (sort) {
            case "createDate": {
                if (asc) {
                    Collections.sort(list, (o1, o2) -> o1.getCreateDate().compareTo(o2.getCreateDate()));
                } else {
                    Collections.sort(list, (o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
                }
                ;
                break;
            }
            case "transferImport": {
                if (asc) {
                    Collections.sort(list, (o1, o2) -> o1.getTransferImport() - (o2.getTransferImport()));
                } else {
                    Collections.sort(list, (o1, o2) -> o2.getTransferImport() - (o1.getTransferImport()));
                }
                ;
                break;
            }
            case "transferExport": {
                if (asc) {
                    Collections.sort(list, (o1, o2) -> o1.getTransferExport() - (o2.getTransferExport()));
                } else {
                    Collections.sort(list, (o1, o2) -> o2.getTransferExport() - (o1.getTransferExport()));
                }
                ;
                break;
            }
            case "productImport": {
                if (asc) {
                    Collections.sort(list, (o1, o2) -> o1.getProductImport() - (o2.getProductImport()));
                } else {
                    Collections.sort(list, (o1, o2) -> o2.getProductImport() - (o1.getProductImport()));
                }
                ;
                break;
            }
            case "productExport": {
                if (asc) {
                    Collections.sort(list, (o1, o2) -> o1.getProductExport() - (o2.getProductExport()));
                } else {
                    Collections.sort(list, (o1, o2) -> o2.getProductExport() - (o1.getProductExport()));
                }
                ;
                break;
            }
            case "proTotalImport": {
                if (asc) {
                    Collections.sort(list, (o1, o2) -> (int) Math.ceil(o1.getProTotalImport() - (o2.getProTotalImport())));
                } else {
                    Collections.sort(list, (o1, o2) -> (int) Math.ceil(o2.getProTotalImport() - (o1.getProTotalImport())));
                }
                ;
                break;
            }
            case "proTotalExport": {
                if (asc) {
                    Collections.sort(list, (o1, o2) -> (int) Math.ceil(o1.getProductExport() - (o2.getProductExport())));
                } else {
                    Collections.sort(list, (o1, o2) -> (int) Math.ceil(o2.getProductExport() - (o1.getProductExport())));
                }
                ;
                break;
            }
            default: {
                Collections.sort(list, (o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
                break;
            }

        }

        statisticDateFormDto.getPage().setCount(getCountPage(list));
        int page = statisticDateFormDto.getPage().getCurrentPage() == 0 ? 0 : statisticDateFormDto.getPage().getCurrentPage() - 1;
        int limit = statisticDateFormDto.getPage().getLimit();
        int start = limit * page, end = limit * (page + 1) <= getCountPage(list) ? limit * (page + 1) : getCountPage(list);

        list = list.subList(start, end);

        return list;

    }

    @Override
    public List<StatisticInventoryTableDto> getSatisticInventory(StatisticInventoryFormDto statisticInventoryFormDto) {
        List<StatisticInventoryTableDto> list = reportDao.getSatisticInventory(statisticInventoryFormDto.getDate(), statisticInventoryFormDto.getInventory(), statisticInventoryFormDto.getUser());
        if (list.size() == 0) return list;

        String sort = statisticInventoryFormDto.getSort().getColumn() == null ? "" : statisticInventoryFormDto.getSort().getColumn();
        boolean asc = statisticInventoryFormDto.getSort().isAsc();

        switch (sort) {
            case "inventory": {
                if (asc) {
                    Collections.sort(list, ((o1, o2) -> o1.getInventory().compareTo(o2.getInventory())));
                } else {
                    Collections.sort(list, ((o1, o2) -> o2.getInventory().compareTo(o1.getInventory())));
                }
                break;
            }
            case "transferImport": {
                if (asc) {
                    Collections.sort(list, ((o1, o2) -> o1.getTransferImport() - o2.getTransferImport()));
                } else {
                    Collections.sort(list, ((o1, o2) -> o2.getTransferImport() - o1.getTransferImport()));
                }
                break;
            }
            case "transferExport": {
                if (asc) {
                    Collections.sort(list, ((o1, o2) -> o1.getTransferExport() - o2.getTransferExport()));
                } else {
                    Collections.sort(list, ((o1, o2) -> o2.getTransferExport() - o1.getTransferExport()));
                }
                break;
            }
            case "productImport": {
                if (asc) {
                    Collections.sort(list, ((o1, o2) -> o1.getProductImport() - o2.getProductImport()));
                } else {
                    Collections.sort(list, ((o1, o2) -> o2.getProductImport() - o1.getProductImport()));
                }
                break;
            }
            case "productExport": {
                if (asc) {
                    Collections.sort(list, ((o1, o2) -> o1.getProductExport() - o2.getProductExport()));
                } else {
                    Collections.sort(list, ((o1, o2) -> o2.getProductExport() - o1.getProductExport()));
                }
            }
            case "proTotalImport": {
                if (asc) {
                    Collections.sort(list, ((o1, o2) -> (int) Math.ceil(o1.getProTotalImport() - o2.getProTotalImport())));
                } else {
                    Collections.sort(list, ((o1, o2) -> (int) Math.ceil(o2.getProTotalImport() - o1.getProTotalImport())));
                }
                break;
            }
            case "proTotalExport": {
                if (asc) {
                    Collections.sort(list, ((o1, o2) -> (int) Math.ceil(o1.getProTotalExport() - o2.getProTotalExport())));
                } else {
                    Collections.sort(list, ((o1, o2) -> (int) Math.ceil(o2.getProTotalExport() - o1.getProTotalExport())));
                }
                break;
            }
            default:{
                Collections.sort(list, ((o1, o2) -> o1.getInventory().compareTo(o2.getInventory())));
                break;
            }
        }

        statisticInventoryFormDto.getPage().setCount(getCountPage(list));

        int page = statisticInventoryFormDto.getPage().getCurrentPage() == 0 ? 0 : statisticInventoryFormDto.getPage().getCurrentPage() - 1;
        int limit = statisticInventoryFormDto.getPage().getLimit();
        int start = limit * page, end = limit * (page + 1) <= getCountPage(list) ? limit * (page + 1) : getCountPage(list);

        list = list.subList(start, end);

        return list;
    }

    @Override
    public <T> int getCountPage(List<T> t) {
        return (int) t.stream().count();
    }

}
