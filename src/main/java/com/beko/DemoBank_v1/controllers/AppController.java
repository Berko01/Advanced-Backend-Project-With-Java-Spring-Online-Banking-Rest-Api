package com.beko.DemoBank_v1.controllers;


import com.beko.DemoBank_v1.models.Account;
import com.beko.DemoBank_v1.models.PaymentHistory;
import com.beko.DemoBank_v1.models.TransactionHistory;
import com.beko.DemoBank_v1.models.User;
import com.beko.DemoBank_v1.repository.AccountRepository;
import com.beko.DemoBank_v1.repository.PaymentHistoryRepository;
import com.beko.DemoBank_v1.repository.TransactHistoryRepository;
import com.beko.DemoBank_v1.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app")
public class AppController {


    @Autowired
    private TransactHistoryRepository transactHistoryRepository;

    @Autowired
    private AppService appService;

    User user;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(HttpSession session) {
        //Get the details of the logged in user:
        user = (User) session.getAttribute("user");

        return appService.getDashboard(user);
    }

    @GetMapping("/payment_history")
    public ResponseEntity<?> getPaymentHistory(HttpSession session) {
        //Get the details of the logged in user:
        user = (User) session.getAttribute("user");

        return appService.getPaymentHistory(user);
    }

    @GetMapping("/transaction_history")
    public ResponseEntity<?> getTransactionHistory(HttpSession session) {
        //Get the details of the logged in user:
        user = (User) session.getAttribute("user");

        return appService.getTransactionHistory(user);
    }


    @PostMapping("/account_transaction_history")
    public ResponseEntity<?> getAccountTransactionHistory(@RequestBody Map<String, String> requestMap, HttpSession session) {

        return appService.getAccountTransactionHistory(requestMap);
    }


}



