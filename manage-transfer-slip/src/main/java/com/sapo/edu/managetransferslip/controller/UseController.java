package com.sapo.edu.managetransferslip.controller;


import com.sapo.edu.managetransferslip.model.dto.Message;
import com.sapo.edu.managetransferslip.model.dto.UsersDTO;
import com.sapo.edu.managetransferslip.model.request.ChangePasswordRequest;
import com.sapo.edu.managetransferslip.model.request.UpdateUserInfoRequest;
import com.sapo.edu.managetransferslip.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/admin/user")
public class UseController {
    private final UserService userService;

    public UseController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<UsersDTO> getAllUser(@RequestParam(value = "page",required = false, defaultValue = "1") Integer page,
                                     @RequestParam(value = "limit",required = false) Integer limit,
                                     @RequestParam(value = "key",required = false) String key){
        if(key == "" || key == null) {
            return userService.getAllUser(page-1, limit);
        }
        else {
            return userService.search(key,page-1,limit);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public ResponseEntity<?> userProfile(@PathVariable int id){
        return userService.getUserInfo(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public ResponseEntity<?> updateUser(@PathVariable int id,@RequestBody UpdateUserInfoRequest updateUserInfoRequest){
        return userService.updateUserInfo(updateUserInfoRequest,id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public Message deleteUser(@PathVariable int id){
        return userService.deleteUser(id);
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public Message changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        return userService.changePassword(changePasswordRequest);
    }

    @GetMapping("countAll")
    @PreAuthorize("hasAnyRole('ROLE_INVENTORIER','ROLE_MANAGER','ROLE_COORDINATOR')")
    public long countAll(){
        return userService.countAll();
    }


}
