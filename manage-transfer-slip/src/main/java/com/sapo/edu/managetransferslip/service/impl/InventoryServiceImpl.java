package com.sapo.edu.managetransferslip.service.impl;


import com.sapo.edu.managetransferslip.model.dto.InventoriesDTO;
import com.sapo.edu.managetransferslip.model.dto.InventoryDetailDTO;
import com.sapo.edu.managetransferslip.repository.InventoryRepository;
import com.sapo.edu.managetransferslip.model.entity.InventoriesEntity;
import com.sapo.edu.managetransferslip.repository.TransferRepository;
import com.sapo.edu.managetransferslip.service.InventoryService;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.awt.desktop.SystemSleepEvent;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final Timestamp date;
    private final ModelMapper modelMapper;
    private final InventoryDetailImpl inventoryDetailImp;
    private final TransferRepository transferRepository;
    public InventoryServiceImpl(InventoryRepository inventoryRepository,InventoryDetailImpl inventoryDetailImp,TransferRepository transferRepository) {
        this.inventoryRepository = inventoryRepository;
        date = new Timestamp(System.currentTimeMillis());
        modelMapper = new ModelMapper();
        this.inventoryDetailImp = inventoryDetailImp;
        this.transferRepository = transferRepository;
    }


    @Override
    public List<InventoriesDTO> getData() {
        List<InventoriesEntity> inventoriesEntities = inventoryRepository.getAll();
        List<InventoriesDTO> inventoriesDTOS = new ArrayList<>();
        for (InventoriesEntity inventoriesEntity :
                inventoriesEntities) {

            inventoriesDTOS.add(convertDTO(inventoriesEntity));
        }
        return inventoriesDTOS;
    }

    @Override
    public ResponseEntity<InventoriesDTO> add(InventoriesDTO inventoriesDTO) {
        if (inventoriesDTO.getCode() == ""){

            inventoriesDTO.setCode(autoRenderCode());
            InventoriesEntity inventoriesEntity = convertEntity(inventoriesDTO);
            inventoriesEntity.setCreateAt(date);
            inventoryRepository.save(inventoriesEntity);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            if (already(inventoriesDTO.getCode()) == false) {
                try {
                    InventoriesEntity inventoriesEntity = convertEntity(inventoriesDTO);
                    inventoriesEntity.setCreateAt(date);
                    inventoryRepository.save(inventoriesEntity);
                    return new ResponseEntity<>(HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
                }
            }
            else{
                InventoriesEntity inventoriesEntity = inventoryRepository.findWasDeleted(inventoriesDTO.getCode());
                if (inventoriesEntity != null){
                    inventoriesEntity.setDeletedAt(null);
                    inventoriesEntity.setAddress(inventoriesDTO.getAddress());
                    inventoriesEntity.setName(inventoriesDTO.getName());
                    inventoriesEntity.setCreateAt(date);
                    inventoriesEntity.setUpdateAt(null);
                    inventoriesEntity.setMail(inventoriesDTO.getMail());
                    inventoriesEntity.setPhone(inventoriesDTO.getPhone());
                    inventoryRepository.save(inventoriesEntity);
                }
            }
        }
        System.out.println(already(inventoriesDTO.getCode()));
        System.out.println(inventoryRepository.findByCode(inventoriesDTO.getCode()).toString());
        System.out.println("already");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public boolean already(String code){
        if (inventoryRepository.findByCode(code) == null)
        {
            return false;
        }
        return true;
    }

    @Override
    public InventoriesDTO findByCode(String code){
        InventoriesDTO inventoriesDTO = convertDTO( inventoryRepository.findByCode(code));
        if (inventoriesDTO != null){
            return inventoriesDTO;
        }
        return null;
    }

    @Override
    public List<Long> year() {
        return inventoryRepository.year();
    }

    @Override
    public List<Long> month() {
        return inventoryRepository.month();
    }

    @Override
    public List<Long> day() {
        return inventoryRepository.day();
    }

    @Override
    public List<InventoriesDTO> findYear(int year) {
        List<InventoriesDTO> inventoriesDTOS = new ArrayList<>();
        List<InventoriesEntity> inventoriesEntities = inventoryRepository.findYear(year);
        if (inventoriesEntities.isEmpty()){
            return null;
        }else{
            for (InventoriesEntity inventoriesEntity :
                    inventoriesEntities) {
                inventoriesDTOS.add(convertDTO(inventoriesEntity));
            }
        }
        return inventoriesDTOS;

    }

    @Override
    public List<InventoriesDTO> findMonth(int month) {
        List<InventoriesDTO> inventoriesDTOS = new ArrayList<>();
        List<InventoriesEntity> inventoriesEntities = inventoryRepository.findMonth(month);
        if (inventoriesEntities.isEmpty()){
            return null;
        }else{
            for (InventoriesEntity inventoriesEntity :
                    inventoriesEntities) {
                inventoriesDTOS.add(convertDTO(inventoriesEntity));
            }
        }
        return inventoriesDTOS;
    }

    @Override
    public List<InventoriesDTO> findDay(int day) {
        List<InventoriesDTO> inventoriesDTOS = new ArrayList<>();
        List<InventoriesEntity> inventoriesEntities = inventoryRepository.findDay(day);
        if (inventoriesEntities.isEmpty()){
            return null;
        }else{
            for (InventoriesEntity inventoriesEntity :
                    inventoriesEntities) {
                inventoriesDTOS.add(convertDTO(inventoriesEntity));
            }
        }
        return inventoriesDTOS;
    }

    @Override
    public List<InventoriesDTO> findFilter(String key, String year, String month, String day) {
        List<InventoriesEntity> inventoriesEntities = inventoryRepository.findFilter(key,year,month,day);
        if (inventoriesEntities.isEmpty())
        {
            return null;
        }
        List<InventoriesDTO> inventoriesDTOS = new ArrayList<>();
       try{
           for (InventoriesEntity inventoriesEntity :
                   inventoriesEntities) {
               inventoriesDTOS.add(convertDTO(inventoriesEntity));
           }
       }catch (Exception e){
           return null;
       }
        return inventoriesDTOS;
    }

    @Override
    public List<InventoriesDTO> findWithoutDay(String key, String year, String month) {
        List<InventoriesEntity> inventoriesEntities = inventoryRepository.findWithoutDay(key,year,month);
        if (inventoriesEntities.isEmpty())
        {
            return null;
        }
        List<InventoriesDTO> inventoriesDTOS = new ArrayList<>();
        try{
            for (InventoriesEntity inventoriesEntity :
                    inventoriesEntities) {
                inventoriesDTOS.add(convertDTO(inventoriesEntity));
            }
        }catch (Exception e){
            return null;
        }
        return inventoriesDTOS;
    }

    @Override
    public List<InventoriesDTO> findWithoutMonth(String key, String year, String day) {
        List<InventoriesEntity> inventoriesEntities = inventoryRepository.findWithoutMonth(key,year,day);
        if (inventoriesEntities.isEmpty())
        {
            return null;
        }
        List<InventoriesDTO> inventoriesDTOS = new ArrayList<>();
        try{
            for (InventoriesEntity inventoriesEntity :
                    inventoriesEntities) {
                inventoriesDTOS.add(convertDTO(inventoriesEntity));
            }
        }catch (Exception e){
            return null;
        }
        return inventoriesDTOS;
    }

    @Override
    public List<InventoriesDTO> findWithoutYear(String key, String month, String day) {
        List<InventoriesEntity> inventoriesEntities = inventoryRepository.findWithoutYear(key,day,month);
        if (inventoriesEntities.isEmpty())
        {
            return null;
        }
        List<InventoriesDTO> inventoriesDTOS = new ArrayList<>();
        try{
            for (InventoriesEntity inventoriesEntity :
                    inventoriesEntities) {
                inventoriesDTOS.add(convertDTO(inventoriesEntity));
            }
        }catch (Exception e){
            return null;
        }
        return inventoriesDTOS;
    }

    @Override
    public List<InventoriesDTO> findWithTime(String year, String month, String day) {
        List<InventoriesEntity> inventoriesEntities = inventoryRepository.findWithTime(year,month,day);
        if (inventoriesEntities.isEmpty())
        {
            return null;
        }
        List<InventoriesDTO> inventoriesDTOS = new ArrayList<>();
        try{
            for (InventoriesEntity inventoriesEntity :
                    inventoriesEntities) {
                inventoriesDTOS.add(convertDTO(inventoriesEntity));
            }
        }catch (Exception e){
            return null;
        }
        return inventoriesDTOS;
    }

    @Override
    public ResponseEntity<InventoriesDTO> addAll(List<InventoriesDTO> inventoriesDTOS) {
        try {
            for (InventoriesDTO inventoriesDTO:
                    inventoriesDTOS) {
                if (already(inventoriesDTO.getCode()) == false){
                    inventoriesDTO.setCreateAt(date);
                    inventoryRepository.save(convertEntity(inventoriesDTO));
                }
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @Override
    public ResponseEntity<InventoriesDTO> delete(int id) {
        try{
            InventoriesEntity inventoriesEntity =  inventoryRepository.findById(id).get();
            if ( inventoriesEntity != null){

                ResponseEntity<InventoryDetailDTO> statusDeleteInventoryDetails = inventoryDetailImp.deleteByInventoryId(inventoriesEntity.getId());
                System.out.println("Status: " + statusDeleteInventoryDetails.getStatusCodeValue());
                if (statusDeleteInventoryDetails.getStatusCodeValue() == 200)
                {
                    inventoriesEntity.setDeletedAt(date);
                    inventoryRepository.save(inventoriesEntity);
                    System.out.println("end");
                    return new ResponseEntity<>(HttpStatus.OK);

                }
               else{
                   if(statusDeleteInventoryDetails.getStatusCodeValue() == 404)
                   {
                       System.out.println(date.toString());
                       System.out.println(inventoriesEntity.getDeletedAt());
                       inventoriesEntity.setDeletedAt(date);
                       System.out.println("after: " + inventoriesEntity.getDeletedAt());
                       System.out.println("All: " + inventoriesEntity.toString());
                       inventoryRepository.save(inventoriesEntity);
                       return new ResponseEntity<>(HttpStatus.OK);
                   }
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public InventoriesDTO find(int id) {

        InventoriesDTO inventoriesDTO = convertDTO(inventoryRepository.findById(id).get());
        if (inventoriesDTO.getDeletedAt() != null)
        {
            return null;
        }
        System.out.println(inventoriesDTO.toString());
        return inventoriesDTO;

    }

    @Override
    public List<InventoriesDTO> findByKey(String key,int page, int limit) {
        try{

            List<InventoriesDTO> pages = new ArrayList<>();
            int start = (page - 1) * limit;
            System.out.println(page);
            List<InventoriesEntity> inventoriesEntities = inventoryRepository.findByKey(key,start,limit);
            System.out.println(inventoriesEntities.size());
            for (InventoriesEntity inventoriesEntity :
                    inventoriesEntities) {
                pages.add(convertDTO(inventoriesEntity));
            }
            return pages;
        } catch (Exception exception) {
            return null;
        }

    }

    @Override
    public long countFinding(String key) {
        long count = 0;
        try{
            count =  inventoryRepository.countFinding(key);
        }catch (Exception e){
            return 0;
        }
        return count;
    }

    @Override
    public List<InventoriesDTO> findExact(String key) {
        try{
            System.out.println(key);
            List<InventoriesEntity> inventoriesEntities = inventoryRepository.findExact(key);
            if (!inventoriesEntities.isEmpty()){
                List<InventoriesDTO> inventoriesDTOS = new ArrayList<>();
                for (InventoriesEntity inventoriesEntity :
                        inventoriesEntities) {
                    inventoriesDTOS.add(convertDTO(inventoriesEntity));
                }
                return inventoriesDTOS;
            }
            return null;
        } catch (Exception exception) {
            return null;
        }
    }


    @Override
    public ResponseEntity<InventoriesDTO> update(InventoriesDTO inventoriesDTO) {

            System.out.println("inventory DTO " + inventoriesDTO.getCode());
            InventoriesEntity inventoriesEntity = inventoryRepository.findById(inventoriesDTO.getId()).get();
            InventoriesEntity check = inventoryRepository.findByCodeOrName(inventoriesDTO.getName());

            if (check != null)
            {
                System.out.println("check");
                System.out.println(check.getCode() != inventoriesEntity.getCode());
               if ( check.getCode() != inventoriesEntity.getCode())
               {
                   System.out.println("if");

                   return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
               }
            }
            inventoriesEntity.setUpdateAt(date);
            inventoriesEntity.setCreateAt(inventoriesDTO.getCreateAt());
            inventoriesEntity.setName(inventoriesDTO.getName());
            Pattern pattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}");
            Matcher matcher = pattern.matcher(inventoriesDTO.getMail());
            boolean mailValidate = matcher.matches();
            if (mailValidate == false)
            {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            inventoriesEntity.setMail(inventoriesDTO.getMail());
            inventoriesEntity.setAddress(inventoriesDTO.getAddress());
            inventoriesEntity.setPhone(inventoriesDTO.getPhone());
            inventoriesEntity.setCode(inventoriesDTO.getCode());
            System.out.println("inventory" + inventoriesEntity.toString());
            try{
                inventoryRepository.save(inventoriesEntity);
                return new ResponseEntity<>(HttpStatus.OK);
            }
          catch (Exception e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
          }


    }

    //pagination
    @Override
    public List<InventoriesDTO> pagination(int page, int limit) {
        List<InventoriesDTO> pages = new ArrayList<>();
        int start = (page - 1) * limit;
        List<InventoriesEntity> inventoriesEntities = inventoryRepository.pagination(start, limit);
        for (InventoriesEntity inventoriesEntity :
                inventoriesEntities) {
            pages.add(convertDTO(inventoriesEntity));
        }
        return pages;
    }

    @Override
    public long countAll() {
        long count = 0;
        try{
           count =  inventoryRepository.countAll();
        }catch (Exception e){
            return 0;
        }
        return count;
    }

    @Override
    public ResponseEntity<InventoriesDTO> updateAll(List<InventoriesDTO> inventoriesDTOS) {
        try {
            for (InventoriesDTO inventoriesDTO :
                    inventoriesDTOS) {
                if (already(inventoriesDTO.getCode()) == true) {
                    InventoriesEntity inventoriesEntity = inventoryRepository.findByCode(inventoriesDTO.getCode());
                    inventoriesEntity.setPhone(inventoriesDTO.getPhone());
                    inventoriesEntity.setUpdateAt(date);
                    inventoriesEntity.setCreateAt(inventoriesDTO.getCreateAt());
                    inventoriesEntity.setDeletedAt(inventoriesDTO.getDeletedAt());
                    inventoriesEntity.setName(inventoriesDTO.getName());
                    inventoriesEntity.setMail(inventoriesDTO.getMail());
                    inventoriesEntity.setCode(inventoriesDTO.getCode());
                    inventoryRepository.save(inventoriesEntity);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    //Convert Type Object data
    public InventoriesDTO convertDTO(InventoriesEntity inventoriesEntity) {
        InventoriesDTO inventoriesDTO = modelMapper.map(inventoriesEntity, InventoriesDTO.class);
        return inventoriesDTO;
    }

    public InventoriesEntity convertEntity(InventoriesDTO inventoriesDTO) {
        InventoriesEntity inventoriesEntity = modelMapper.map(inventoriesDTO, InventoriesEntity.class);
        return inventoriesEntity;
    }

  public String autoRenderCode(){
        InventoriesEntity inventoriesEntity = inventoryRepository.findLastElement();
        String code = "inv00" + String.valueOf(inventoriesEntity.getId() + 1) ;
        System.out.println("code: " + code);
        System.out.println("already: " + already(code));
        if (already(code) == true){
            while (already(code)){
                int length = inventoriesEntity.getCode().length();
                Random rand = new Random();
                char[] randomCode = new char[length];
                for (int i = 0; i< length; i++){
                    randomCode[i] = inventoriesEntity.getCode().charAt(rand.nextInt(length));
                }
                code = String.valueOf(randomCode);

            }
        }
        return code;
  }


}
