package com.sapo.edu.managetransferslip.model.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private int id;
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
