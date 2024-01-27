package com.beko.DemoBank_v1.service.impl;

import com.beko.DemoBank_v1.repository.UserRepository;
import com.beko.DemoBank_v1.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {

    private final UserRepository userRepository;

    @Autowired
    public IndexServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> getVerify(String token, String code) {
        try {
            // Get Token In Database
            String dbToken = userRepository.checkToken(token);

            // Check If Token Is Valid:
            if (dbToken == null) {
                return ResponseEntity.badRequest().body("This session has expired.");
            }

            // Update and Verify Account
            userRepository.verifyAccount(token, code);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Verification success.");
            System.out.println("In Verify Account Controller");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error verifying account: " + e.getMessage());
        }
    }
}
