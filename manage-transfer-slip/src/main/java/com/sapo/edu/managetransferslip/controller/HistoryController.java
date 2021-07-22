package com.sapo.edu.managetransferslip.controller;

import com.sapo.edu.managetransferslip.model.dto.HistoryDTO;
import com.sapo.edu.managetransferslip.service.HistoryServices;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@PreAuthorize("hasRole('ROLE_MANAGER')")
@RequestMapping("/admin/history")
public class HistoryController {

    private HistoryServices historyService;

    public HistoryController(HistoryServices historyService) {
        this.historyService = historyService;
    }

    @GetMapping()
    public List<HistoryDTO> getData(@RequestParam int id, @RequestParam int page, @RequestParam int limit){
        return historyService.getData(id,page,limit);
    }

    @GetMapping("/size")
    public long size(@RequestParam int id){
        return historyService.sizeHistory(id);
    }

    @GetMapping("/finding")
    public List<HistoryDTO> finding(@RequestParam int id, @RequestParam String key,@RequestParam int page,@RequestParam int limit){
        System.out.println("id: " + id);
        System.out.println("key: " + key);
        System.out.println("page: " + page);
        System.out.println("limit: " + limit);
        return historyService.finding(id,key,page,limit);
    }

    @GetMapping("/finding/size")
    public long sizeFinding(@RequestParam int id,@RequestParam String key){
        return historyService.sizeFinding(id,key);
    }

    @GetMapping("/filter")
    public List<HistoryDTO> filterTime(@RequestParam int page,@RequestParam int limit,@RequestParam int id
            ,@RequestParam String key,@RequestParam String from,@RequestParam String to)
    {
        return historyService.findingByTime(id,key,from,to,page,limit);
    }

    @GetMapping("/filter/size")
    public long filterTimeSize(@RequestParam int id,@RequestParam String key,@RequestParam String from,@RequestParam String to)
    {
        return historyService.sizeByTime(id,key,from,to);
    }

    //export
    @GetMapping("/export")
    public List<HistoryDTO> getDataExport(@RequestParam int id, @RequestParam int page, @RequestParam int limit){
        return historyService.getDataExport(id,page,limit);
    }

    @GetMapping("/export/size")
    public long sizeExport(@RequestParam int id){
        return historyService.sizeExport(id);
    }

    @GetMapping("/export/finding")
    public List<HistoryDTO> findingExport(@RequestParam int id, @RequestParam String key,@RequestParam int page,@RequestParam int limit){
        return historyService.findingExport(id,key,page,limit);
    }

    @GetMapping("/export/finding/size")
    public long sizeFindingExport(@RequestParam int id,@RequestParam String key){
        return historyService.sizeFindingExport(id,key);
    }

    @GetMapping("/export/filter")
    public List<HistoryDTO> filterTimeExport(@RequestParam int page,@RequestParam int limit,@RequestParam int id
            ,@RequestParam String key,@RequestParam String from,@RequestParam String to)
    {
        System.out.println("id: " + id);
        System.out.println("key: " + key);
        System.out.println("from: " + from);
        System.out.println("to: " + to);
        System.out.println("page: " + limit);
        return historyService.findingByTimeExport(id,key,from,to,page,limit);
    }

    @GetMapping("/export/filter/size")
    public long filterTimeSizeExport(@RequestParam int id,@RequestParam String key,@RequestParam String from,@RequestParam String to)
    {
        return historyService.sizeByTimeExport(id,key,from,to);
    }

    //import
    @GetMapping("/import")
    public List<HistoryDTO> getDataImport(@RequestParam int id, @RequestParam int page, @RequestParam int limit){
        System.out.println("import ");
        return historyService.getDataImport(id,page,limit);
    }

    @GetMapping("/import/size")
    public long sizeImport(@RequestParam int id){
        return historyService.sizeImport(id);
    }

    @GetMapping("/import/finding")
    public List<HistoryDTO> findingImport(@RequestParam int id, @RequestParam String key,@RequestParam int page,@RequestParam int limit){
        return historyService.findingImport(id,key,page,limit);
    }

    @GetMapping("/import/finding/size")
    public long sizeFindingImport(@RequestParam int id,@RequestParam String key){
        return historyService.sizeFindingImport(id,key);
    }

    @GetMapping("/import/filter")
    public List<HistoryDTO> filterTimeImport(@RequestParam int page,@RequestParam int limit,@RequestParam int id
            ,@RequestParam String key,@RequestParam String from,@RequestParam String to)
    {
        System.out.println("id: " + id);
        System.out.println("key: " + key);
        System.out.println("from: " + from);
        System.out.println("to: " + to);
        System.out.println("page: " + limit);
        return historyService.findingByTimeImport(id,key,from,to,page,limit);
    }

    @GetMapping("/import/filter/size")
    public long filterTimeSizeImport(@RequestParam int id,@RequestParam String key,@RequestParam String from,@RequestParam String to)
    {
        return historyService.sizeByTimeImport(id,key,from,to);
    }
}
