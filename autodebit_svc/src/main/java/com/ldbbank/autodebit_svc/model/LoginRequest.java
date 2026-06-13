package com.ldbbank.autodebit_svc.model;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonAlias;

@Data
public class LoginRequest {
    @JsonAlias({"USER_NAME", "userName", "username"})
    private String userName;

    @JsonAlias({"PASSWORD", "password"})
    private String password;
}
