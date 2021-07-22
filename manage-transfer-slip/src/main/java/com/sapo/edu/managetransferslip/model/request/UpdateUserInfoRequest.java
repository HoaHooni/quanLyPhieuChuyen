package com.sapo.edu.managetransferslip.model.request;

import lombok.Data;

import java.sql.Date;
import java.util.Set;

@Data
public class UpdateUserInfoRequest {

    private String email;
    private String phone;
    private Date dob;
    private String address;
    private Set<String> roles;
}
