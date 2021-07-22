package com.sapo.edu.managetransferslip.service;

import com.sapo.edu.managetransferslip.model.dto.Message;
import com.sapo.edu.managetransferslip.model.dto.UsersDTO;
import com.sapo.edu.managetransferslip.model.entity.UsersEntity;
import com.sapo.edu.managetransferslip.model.request.ChangePasswordRequest;
import com.sapo.edu.managetransferslip.model.request.SignupRequest;
import com.sapo.edu.managetransferslip.model.request.UpdateUserInfoRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    UsersEntity getByUsername(String username);

    ResponseEntity<?> signUp(SignupRequest signupRequest);

    Message changePassword(ChangePasswordRequest changePasswordRequest);

    List<UsersDTO> getAllUser(Integer page, Integer limit);

    ResponseEntity<?> updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest, Integer id);

    ResponseEntity<?> getUserInfo(Integer id);

    Message deleteUser(Integer id);

    List<UsersDTO> search(String key,Integer page, Integer limit);

    long countAll();
}
