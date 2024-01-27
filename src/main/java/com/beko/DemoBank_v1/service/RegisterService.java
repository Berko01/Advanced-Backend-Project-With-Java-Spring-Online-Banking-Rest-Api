package com.beko.DemoBank_v1.service;

import com.beko.DemoBank_v1.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
public interface RegisterService {
    ResponseEntity<?> registerUser(User user, String confirmPassword);
}
