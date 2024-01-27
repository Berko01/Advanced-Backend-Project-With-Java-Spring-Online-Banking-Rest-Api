package com.beko.DemoBank_v1.service;

import com.beko.DemoBank_v1.models.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AccountService {
    ResponseEntity createAccount(Map<String, String> requestMap, User user);
}