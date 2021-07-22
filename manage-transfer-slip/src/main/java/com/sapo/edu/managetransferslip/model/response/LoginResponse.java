package com.sapo.edu.managetransferslip.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String token;
    private String type = "Bearer";
    private int id;
    private String username;
    private String email;
    private List<String> roles;
    private int inventoryId;

    public LoginResponse(String accessToken, Integer id, String username, String email, List<String> roles, int inventoryId) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.inventoryId = inventoryId;
    }
}
