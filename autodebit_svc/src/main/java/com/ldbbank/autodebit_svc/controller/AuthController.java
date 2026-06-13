package com.ldbbank.autodebit_svc.controller;

import com.ldbbank.autodebit_svc.model.LoginRequest;
import com.ldbbank.autodebit_svc.model.LoginResponse;
import com.ldbbank.autodebit_svc.service.AuthService;
import com.ldbbank.autodebit_svc.db.autodebit.entity.UserDbEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.MapUserMenuEntity;
import com.ldbbank.autodebit_svc.db.autodebit.entity.VvUserEntity;
import com.ldbbank.autodebit_svc.db.autodebit.repository.MapUserMenuRepository;
import com.ldbbank.autodebit_svc.db.autodebit.repository.VvUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final MapUserMenuRepository mapUserMenuRepository;
    private final VvUserRepository vvUserRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Optional<UserDbEntity> userOpt = authService.authenticate(req.getUserName(), req.getPassword());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("code", "01", "message", "ທ່ານປ້ອນຊື່ ຫຼື ລະຫັດຜ່ານບໍ່ໂືກຕ້ອງ !!!!"));
        }
        UserDbEntity user = userOpt.get();
        String access = authService.createAccessToken(user);
        String refresh = authService.createRefreshToken(user.getUserName());

        LoginResponse resp = new LoginResponse();
        resp.setUserId(user.getUserId());
        resp.setUserName(user.getUserName());
        resp.setName(user.getName());
        resp.setAccessToken("Bearer " + access);
        resp.setRefreshToken(refresh);
        // build menu from vvUserRepository.findByUserName(user.getUserName())
        List<VvUserEntity> menus = vvUserRepository.findByUserName(user.getUserName());
        List<Map<String, Object>> menu = new ArrayList<>();
        if (menus != null) {
            for (VvUserEntity m : menus) {
                menu.add(Map.of(
                        "id", m.getMenuNo(),
                        "label", m.getMenuName(),
                        "path", m.getMenuPath(),
                        "icon", m.getMenuIcon()
                ));
            }
        }
        resp.setMenu(menu);



        return ResponseEntity.ok(Map.of("code", "00", "message", "Success", "data", resp));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null) return ResponseEntity.badRequest().body(Map.of("code", "02", "message", "refreshToken missing"));
        Optional<String> newAccess = authService.refreshAccessToken(refreshToken);
        if (newAccess.isEmpty()) return ResponseEntity.status(401).body(Map.of("code", "03", "message", "Invalid or expired refresh token"));
        return ResponseEntity.ok(Map.of("code", "00", "message", "Success", "accessToken", "Bearer " + newAccess.get()));
    }

    @PostMapping("/hash")
    public ResponseEntity<?> hashPassword(@RequestBody Map<String, String> body) {
        String password = body.get("password");
        if (password == null) return ResponseEntity.badRequest().body(Map.of("code", "04", "message", "password missing"));
        String hashed = new BCryptPasswordEncoder().encode(password);
        return ResponseEntity.ok(Map.of("code", "00", "message", "Success", "hash", hashed));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPassword(@RequestBody Map<String, String> body) {
        String password = body.get("password");
        String hash = body.get("hash");
        if (password == null || hash == null) return ResponseEntity.badRequest().body(Map.of("code", "05", "message", "password or hash missing"));
        boolean matches = new BCryptPasswordEncoder().matches(password, hash);
        if (matches) return ResponseEntity.ok(Map.of("code", "00", "message", "Match"));
        return ResponseEntity.status(401).body(Map.of("code", "01", "message", "Not match"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body) {
        String username = Optional.ofNullable(body.get("USER_NAME")).orElse(body.get("userName"));
        String oldPassword = Optional.ofNullable(body.get("OLD_PASSWORD")).orElse(body.get("oldPassword"));
        String newPassword = Optional.ofNullable(body.get("NEW_PASSWORD")).orElse(body.get("newPassword"));
        if (username == null || oldPassword == null || newPassword == null) {
            return ResponseEntity.badRequest().body(Map.of("code", "06", "message", "missing parameters"));
        }
        boolean ok = authService.changePassword(username, oldPassword, newPassword);
        if (!ok) {
            return ResponseEntity.status(401).body(Map.of("code", "01", "message", "username or old password ບໍ່ຖືກຕ້ອງ!!!"));
        }
        return ResponseEntity.ok(Map.of("code", "00", "message", "ທ່ານປ່ຽນລະຫັດຜ່ານສໍາເລັດ !!!"));
    }
}

