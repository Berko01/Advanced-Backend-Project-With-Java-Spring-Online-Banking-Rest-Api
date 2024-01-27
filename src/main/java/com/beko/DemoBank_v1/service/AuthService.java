package com.beko.DemoBank_v1.service;


import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface AuthService {
    ResponseEntity<?> login(String email, String password,
                            HttpSession session, HttpServletResponse response);

    ResponseEntity<?> logout(HttpSession session);
}
