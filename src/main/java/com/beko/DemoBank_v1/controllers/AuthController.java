package com.beko.DemoBank_v1.controllers;

import com.beko.DemoBank_v1.helpers.Token;
import com.beko.DemoBank_v1.helpers.authorization.JwtService;
import com.beko.DemoBank_v1.models.User;
import com.beko.DemoBank_v1.repository.UserRepository;
import com.beko.DemoBank_v1.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    public AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> requestMap ,
                                   HttpSession session, HttpServletResponse response){

        String email = requestMap.get("email");
        String password = requestMap.get("password");

        return authService.login(email, password, session, response);
    }
    //End of Authentication

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session){
        return authService.logout(session);
    }
}
