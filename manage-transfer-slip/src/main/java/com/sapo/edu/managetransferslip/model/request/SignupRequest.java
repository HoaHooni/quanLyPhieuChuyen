package com.sapo.edu.managetransferslip.model.request;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;
@Data
public class SignupRequest {
    private String code;
    private String username;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Date dob;
    private Timestamp createAt;
    private Timestamp updateAt;
    private Timestamp deleteAt;
    private int inventoryId;
    private Set<String> role;
}
