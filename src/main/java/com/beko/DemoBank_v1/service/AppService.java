package com.beko.DemoBank_v1.service;

import com.beko.DemoBank_v1.models.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AppService {
    ResponseEntity<?> getDashboard(User user);
    ResponseEntity<?> getPaymentHistory(User user);
    ResponseEntity<?> getTransactionHistory(User user);

    ResponseEntity<?> getAccountTransactionHistory(Map<String,String> requestMap);
}
