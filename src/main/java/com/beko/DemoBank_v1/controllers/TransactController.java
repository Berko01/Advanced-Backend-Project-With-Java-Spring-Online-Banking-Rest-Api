package com.beko.DemoBank_v1.controllers;

import com.beko.DemoBank_v1.models.PaymentRequest;
import com.beko.DemoBank_v1.models.TransferRequest;
import com.beko.DemoBank_v1.models.User;
import com.beko.DemoBank_v1.repository.AccountRepository;
import com.beko.DemoBank_v1.repository.PaymentRepository;
import com.beko.DemoBank_v1.repository.TransactRepository;
import com.beko.DemoBank_v1.service.TransactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/transact")
public class TransactController {
    User user;

    @Autowired
    private TransactService transactService;


    @PostMapping("/deposit")
    public ResponseEntity deposit(@RequestBody Map<String, String> requestMap, HttpSession session) {

        //TODO: GET LOGGED INT USER:

        user = (User) session.getAttribute("user");

        return transactService.deposit(requestMap, user);

    }
    //END OF TRANSFER METHOD

    @PostMapping("/withdraw")
    ResponseEntity transfer(@RequestBody Map<String, String> requestMap, HttpSession session) {

        //TODO: GET LOGGED IN USER:
        user = (User) session.getAttribute("user");

        return transactService.withdraw(requestMap, user);
    }
    //End Of Withdrawal Method.

    @PostMapping("/payment")
    ResponseEntity transfer(@RequestBody PaymentRequest request, HttpSession session) {

        //TODO: GET LOGGED IN USER:
        user = (User) session.getAttribute("user");

        return transactService.payment(request, user);

    }


    //End Of Payment Method.

    @PostMapping("/transfer")
    ResponseEntity transfer(@RequestBody TransferRequest request, HttpSession session) {
        //TODO: GET LOGGED IN USER:
        user = (User) session.getAttribute("user");

        return transactService.transfer(request, user);


    }
    //END OF TRANSFER METHOD
}
