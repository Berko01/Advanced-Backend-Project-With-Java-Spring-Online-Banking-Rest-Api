package com.beko.DemoBank_v1.controllers;

import com.beko.DemoBank_v1.models.User;
import com.beko.DemoBank_v1.repository.UserRepository;
import com.beko.DemoBank_v1.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

@RestController
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult bindingResult, @RequestParam("confirm_password") String confirmPassword) {

        if(bindingResult.hasErrors() && confirmPassword.isEmpty()){
            List<String> errorMessages = new ArrayList<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errorMessages.add(error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body(errorMessages);
        }

        return registerService.registerUser(user, confirmPassword);

    }
}
