package com.beko.DemoBank_v1.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


public interface IndexService {
    ResponseEntity<?> getVerify(String token, String code);
}
