package com.beko.DemoBank_v1.service.impl;

import com.beko.DemoBank_v1.helpers.Token;
import com.beko.DemoBank_v1.helpers.authorization.JwtService;
import com.beko.DemoBank_v1.models.User;
import com.beko.DemoBank_v1.repository.UserRepository;
import com.beko.DemoBank_v1.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<?> login(String email, String password, HttpSession session, HttpServletResponse response) {
        try {
            validateInputFields(email, password);

            String userEmailInDatabase = userRepository.getUserEmail(email);

            if (userEmailInDatabase == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect Username or Password");
            }

            String passwordInDatabase = userRepository.getUserPassword(userEmailInDatabase);

            if (!BCrypt.checkpw(password, passwordInDatabase)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect Username or Password");
            }

            int verified = userRepository.isVerified(userEmailInDatabase);

            if (verified != 1) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account verification required.");
            }

            User user = userRepository.getUserDetails(userEmailInDatabase);
            String jwt = jwtService.generateToken(user.getEmail());

            // Token'i JSON yanıtının içine ekleyin
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Authentication confirmed");
            responseBody.put("access_token", jwt);

            session.setAttribute("user", user);
            session.setAttribute("token", jwt);
            session.setAttribute("authenticated", true);

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @Override
    public ResponseEntity<?> logout(HttpSession session) {
        try {
            session.invalidate();
            return ResponseEntity.ok("Logged out successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    private void validateInputFields(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Username or Password Cannot Be Empty.");
        }
    }
}
