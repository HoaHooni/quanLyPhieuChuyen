package com.sapo.edu.managetransferslip.service.impl;

import com.sapo.edu.managetransferslip.model.dto.Message;
import com.sapo.edu.managetransferslip.model.dto.UsersDTO;
import com.sapo.edu.managetransferslip.model.entity.*;
import com.sapo.edu.managetransferslip.model.request.ChangePasswordRequest;
import com.sapo.edu.managetransferslip.model.request.SignupRequest;
import com.sapo.edu.managetransferslip.model.request.UpdateUserInfoRequest;
import com.sapo.edu.managetransferslip.repository.RoleRepository;
import com.sapo.edu.managetransferslip.repository.UserRepository;
import com.sapo.edu.managetransferslip.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.util.*;
import java.sql.Date;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public UsersEntity getByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public ResponseEntity<?> signUp(SignupRequest signupRequest) {

        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new Message("Error: Username da ton tai"));
        }
        if(signupRequest.getCode() == ""){
            UsersEntity usersEntity = userRepository.findFirstByOrderByIdDesc();
            signupRequest.setCode(("USER" + String.valueOf(usersEntity.getId() + 1) ));
        }
        else {
            if (userRepository.existsByCode(signupRequest.getCode())) {
                return ResponseEntity.badRequest().body(new Message("Error: Code da ton tai"));
            }
        }
        System.out.println("aa"+signupRequest.getCode());

        UsersEntity user = new UsersEntity( signupRequest.getCode(),signupRequest.getUsername(), passwordEncoder.encode(signupRequest.getPassword()), signupRequest.getEmail(),signupRequest.getPhone()
                ,signupRequest.getAddress(),signupRequest.getDob(),1,signupRequest.getCreateAt(),signupRequest.getUpdateAt(),signupRequest.getDeleteAt(),signupRequest.getInventoryId());

        Set<String> strRoles = signupRequest.getRole();
        Set<RolesEntity> roles = new HashSet<>();
        if (strRoles == null) {
            RolesEntity userRole = roleRepository.findByName(ERole.ROLE_INVENTORIER.name()).orElseThrow(() -> new RuntimeException("Error: Role not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ROLE_MANAGER":
                        RolesEntity managerRole = roleRepository.findByName(ERole.ROLE_MANAGER.name()).orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(managerRole);
                        break;
                    case "ROLE_COORDINATOR":
                        RolesEntity coordinatorRole = roleRepository.findByName(ERole.ROLE_COORDINATOR.name()).orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(coordinatorRole);
                        break;
                    case "ROLE_INVENTORIER":
                        RolesEntity userRole = roleRepository.findByName(ERole.ROLE_INVENTORIER.name()).orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(userRole);
                        break;
                }

            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new Message("Success!"));
    }

    @Override
    public Message changePassword(ChangePasswordRequest changePasswordRequest) {
        UsersEntity user = userRepository.getById(changePasswordRequest.getId());
        if (user == null) {
            return new Message("USER_NOT_FOUND");
        }
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            return new Message("OLD_PASSWORD_NOT_CORRECT");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return new Message("SUCCESS");
    }

    @Override
    public List<UsersDTO> getAllUser(Integer page, Integer limit) {
        List<UsersEntity> usersEntityList;
        if(page != null && limit != null){
            Page<UsersEntity> usersEntityPage = userRepository.findAllByDeleteAtIsNullOrderByIdDesc(PageRequest.of(page, limit));
            usersEntityList = usersEntityPage.toList();

        }
        else {
            usersEntityList = userRepository.findAllByDeleteAtIsNullOrderByIdDesc();
        }
        List<UsersDTO> usersDTOList = new ArrayList<>();

        for(UsersEntity usersEntity: usersEntityList){
            UsersDTO usersDTO = new UsersDTO(usersEntity);

            usersDTOList.add(usersDTO);
        }
        return usersDTOList;
    }

    @Override
    public ResponseEntity<?> updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest,Integer id) {


        UsersEntity usersEntity = userRepository.getById(id);
        usersEntity.setEmail(updateUserInfoRequest.getEmail());
        usersEntity.setAddress(updateUserInfoRequest.getAddress());
        usersEntity.setDob(updateUserInfoRequest.getDob());
        usersEntity.setPhone(updateUserInfoRequest.getPhone());

        Set<String> strRoles = updateUserInfoRequest.getRoles();
        Set<RolesEntity> roles = new HashSet<>();
        if (strRoles == null) {
            RolesEntity userRole = roleRepository.findByName(ERole.ROLE_INVENTORIER.name()).orElseThrow(() -> new RuntimeException("Error: Role not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ROLE_MANAGER":
                        RolesEntity managerRole = roleRepository.findByName(ERole.ROLE_MANAGER.name()).orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(managerRole);
                        break;
                    case "ROLE_COORDINATOR":
                        RolesEntity coordinatorRole = roleRepository.findByName(ERole.ROLE_COORDINATOR.name()).orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(coordinatorRole);
                        break;
                    case "ROLE_INVENTORIER":
                        RolesEntity userRole = roleRepository.findByName(ERole.ROLE_INVENTORIER.name()).orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(userRole);
                        break;
                }

            });
        }
        usersEntity.setRoles(roles);

        userRepository.save(usersEntity);

        UsersDTO usersDTO = new UsersDTO(usersEntity);



        return ResponseEntity.ok(usersDTO) ;
    }

    @Override
    public ResponseEntity<?> getUserInfo(Integer id) {
        UsersEntity usersEntity = userRepository.findByIdAndDeleteAtIsNull(id);


        UsersDTO usersDTO = new UsersDTO(usersEntity);

        return ResponseEntity.ok(usersDTO) ;
    }

    @Override
    public Message deleteUser(Integer id) {
        try{
            UsersEntity usersEntity= userRepository.findByIdAndDeleteAtIsNull(id);
            if(usersEntity !=null){
                usersEntity.setDeleteAt(new Timestamp(System.currentTimeMillis()));

                userRepository.save(usersEntity);
                return new Message("Delete successfully, userID: " + id);
            }else{
                return new Message("userID: " + id+" is not found.");
            }
        } catch (Exception e) {
            return new Message("Delete failed.");
        }
    }

    @Override
    public List<UsersDTO> search(String key, Integer page, Integer limit) {
        List<UsersEntity> usersEntityList;
        if(page != null && limit != null){
            int start = page * limit;
             usersEntityList = userRepository.searchUserByKey(key,start,limit);


        }
        else {
            usersEntityList = userRepository.searchUserByKey(key);
        }
        List<UsersDTO> usersDTOList = new ArrayList<>();

        for(UsersEntity usersEntity: usersEntityList){
            UsersDTO usersDTO = new UsersDTO(usersEntity);

            usersDTOList.add(usersDTO);
        }
        return usersDTOList;
    }

    @Override
    public long countAll() {
        return userRepository.count();
    }
}
