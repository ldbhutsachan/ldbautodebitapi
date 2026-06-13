package com.ldbbank.autodebit_svc._framwork;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ldbbank.autodebit_svc.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@Order(1)
public class JwtAuthFilter implements jakarta.servlet.Filter {

    private static final String[] WHITELIST = new String[] {
            "/auth/login",
            "/auth/refresh",
            "/auth/hash",
            "/auth/verify",
            "/swagger",
            "/swagger-ui",
            "/v3/api-docs"
    };

    private boolean isWhitelisted(String path) {
        if (path == null) return false;
        for (String p : WHITELIST) {
            if (path.startsWith(p)) return true;
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        String context = req.getContextPath();
        if (context != null && !context.isEmpty() && path.startsWith(context)) {
            path = path.substring(context.length());
        }

        if (isWhitelisted(path)) {
            chain.doFilter(request, response);
            return;
        }

        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            sendUnauthorized(res, "Missing or invalid Authorization header");
            return;
        }
        String token = auth.substring(7);
        boolean valid = JwtTokenUtil.validateToken(token);
        if (!valid) {
            sendUnauthorized(res, "Invalid or expired token");
            return;
        }

        chain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse res, String message) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json;charset=UTF-8");
        Map<String, String> body = Map.of("code", "401", "message", message);
        new ObjectMapper().writeValue(res.getWriter(), body);
    }
}
