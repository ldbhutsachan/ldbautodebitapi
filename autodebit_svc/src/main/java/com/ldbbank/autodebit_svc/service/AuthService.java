package com.ldbbank.autodebit_svc.service;

import com.ldbbank.autodebit_svc.db.autodebit.entity.UserDbEntity;
import com.ldbbank.autodebit_svc.db.autodebit.repository.UserDbRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final UserDbRepository userRepo;
    // simple in-memory refresh token store: refreshToken -> (username, expiryEpoch)
    private final Map<String, RefreshInfo> refreshStore = new ConcurrentHashMap<>();

    public AuthService(UserDbRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<UserDbEntity> authenticate(String username, String password) {
        if (username == null || password == null) return Optional.empty();
        return userRepo.findByUserName(username)
                .filter(u -> {
                    String stored = u.getPassword();
                    if (stored == null) return false;
                    // if stored looks like a bcrypt hash, use encoder; otherwise compare plaintext
                   //to do
                    return stored.equals(password);
                });
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        if (username == null || oldPassword == null || newPassword == null) return false;
        Optional<UserDbEntity> uOpt = userRepo.findByUserName(username);
        if (uOpt.isEmpty()) return false;
        UserDbEntity user = uOpt.get();
        String stored = user.getPassword();
        if (stored == null) return false;
        boolean matches;

        if(!user.getPassword().equals(oldPassword)){
            matches = false;
        }else {
            matches = true;
        }
        if (!matches) return false;

        user.setPassword(newPassword);
        userRepo.save(user);
        return true;
    }

    public String createAccessToken(UserDbEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("userName", user.getUserName());
        claims.put("name", user.getName());
        // 30 minutes expiry
        return com.ldbbank.autodebit_svc.util.JwtTokenUtil.generateToken(claims, String.valueOf(user.getUserId()), Duration.ofMinutes(30));
    }

    public String createRefreshToken(String username) {
        String token = UUID.randomUUID().toString();
        long expiry = Instant.now().plus(Duration.ofDays(7)).getEpochSecond();
        refreshStore.put(token, new RefreshInfo(username, expiry));
        return token;
    }

    public Optional<String> refreshAccessToken(String refreshToken) {
        RefreshInfo info = refreshStore.get(refreshToken);
        if (info == null) return Optional.empty();
        if (Instant.now().getEpochSecond() > info.expiryEpoch) {
            refreshStore.remove(refreshToken);
            return Optional.empty();
        }
        Optional<UserDbEntity> userOpt = userRepo.findByUserName(info.username);
        return userOpt.map(this::createAccessToken);
    }

    private static class RefreshInfo {
        final String username;
        final long expiryEpoch;

        RefreshInfo(String username, long expiryEpoch) {
            this.username = username;
            this.expiryEpoch = expiryEpoch;
        }
    }
}
