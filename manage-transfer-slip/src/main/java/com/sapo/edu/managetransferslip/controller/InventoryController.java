package com.sapo.edu.managetransferslip.controller;

import com.sapo.edu.managetransferslip.model.dto.InventoriesDTO;
import com.sapo.edu.managetransferslip.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@PreAuthorize("hasRole('ROLE_MANAGER')")
@RequestMapping("/admin/inventory")
public class InventoryController {


    private InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_COORDINATOR')")
    public List<InventoriesDTO> getData() {
        System.out.println("here");
        return inventoryService.getData();
    }

    //done
    @PostMapping()
    public ResponseEntity<InventoriesDTO> add(@RequestBody InventoriesDTO inventoriesDTO) {

        return inventoryService.add(inventoriesDTO);
    }

    //done
    @PutMapping()
    public ResponseEntity<InventoriesDTO> update(@RequestBody InventoriesDTO inventoriesDTO) {
        return inventoryService.update(inventoriesDTO);
    }

    //alive
    @DeleteMapping("/{id}")
    public ResponseEntity<InventoriesDTO> delete(@PathVariable("id") int id) {
        return inventoryService.delete(id);
    }

    //done
    @GetMapping("/{id}")
    public InventoriesDTO find(@PathVariable("id") int id) {
        return inventoryService.find(id);
    }

    @GetMapping("/find-by-key")
    public List<InventoriesDTO> findByKey(@RequestParam String key, @RequestParam int page, @RequestParam int limit) {
        return inventoryService.findByKey(key, page, limit);
    }

    @GetMapping("/find-key")
    public List<InventoriesDTO> findExact(@RequestParam String key) {
        return inventoryService.findExact(key);
    }

    //done
    @GetMapping("/page")
    public List<InventoriesDTO> pages(@RequestParam int page, @RequestParam int limit) {
        return inventoryService.pagination(page, limit);
    }

    ;

    //done
    @GetMapping("/size")
    public long size() {
        return inventoryService.countAll();
    }

    //size finding
    @GetMapping("/size-finding")
    public long sizeFinding(String key) {
        return inventoryService.countFinding(key);
    }

    //done
    @GetMapping("/find-by-code")
    public InventoriesDTO findByCode(String code) {
        return inventoryService.findByCode(code);
    }


    @GetMapping("/find-day")
    public List<InventoriesDTO> findDay(@RequestParam int day) {
        return inventoryService.findDay(day);
    }

    @GetMapping("/find-month")
    public List<InventoriesDTO> findMonth(@RequestParam int month) {
        return inventoryService.findMonth(month);
    }

    @GetMapping("/find-year")
    public List<InventoriesDTO> findYear(@RequestParam int year) {
        return inventoryService.findYear(year);
    }

    @GetMapping("/find-filter")
    public List<InventoriesDTO> findFilter(@RequestParam String key, @RequestParam String year, @RequestParam String month, @RequestParam String day) {
        return inventoryService.findFilter(key, year, month, day);
    }


    @GetMapping("/find-without-day")
    public List<InventoriesDTO> findWithoutDay(@RequestParam String key, @RequestParam String year, @RequestParam String month) {
        return inventoryService.findWithoutDay(key, year, month);
    }

    @GetMapping("/find-without-month")
    public List<InventoriesDTO> findWithoutMonth(@RequestParam String key, @RequestParam String year, @RequestParam String day) {
        return inventoryService.findWithoutMonth(key, year, day);
    }

    @GetMapping("/find-without-year")
    public List<InventoriesDTO> findWithoutYear(@RequestParam String key, @RequestParam String month, @RequestParam String day) {
        return inventoryService.findWithoutYear(key, month, day);
    }

    @GetMapping("/find-with-time")
    public List<InventoriesDTO> findWithTime(@RequestParam String year, @RequestParam String month, @RequestParam String day) {
        System.out.println(year + " " + month + " " + day);
        return inventoryService.findWithTime(year, month, day);
    }


}
