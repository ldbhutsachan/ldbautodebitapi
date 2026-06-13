package com.ldbbank.autodebit_svc.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LoginResponse {
    private Long userId;
    private String userName;
    private String name;
    private String accessToken; // Bearer token
    private String refreshToken;
    private List<Map<String, Object>> menu;
}
