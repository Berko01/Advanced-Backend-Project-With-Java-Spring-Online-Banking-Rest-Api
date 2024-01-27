package com.beko.DemoBank_v1.service.impl;

import com.beko.DemoBank_v1.models.Account;
import com.beko.DemoBank_v1.models.PaymentHistory;
import com.beko.DemoBank_v1.models.TransactionHistory;
import com.beko.DemoBank_v1.models.User;
import com.beko.DemoBank_v1.repository.AccountRepository;
import com.beko.DemoBank_v1.repository.PaymentHistoryRepository;
import com.beko.DemoBank_v1.repository.TransactHistoryRepository;
import com.beko.DemoBank_v1.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppServiceImpl implements AppService {

    private final AccountRepository accountRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final TransactHistoryRepository transactHistoryRepository;

    @Autowired
    public AppServiceImpl(AccountRepository accountRepository, PaymentHistoryRepository paymentHistoryRepository, TransactHistoryRepository transactHistoryRepository) {
        this.accountRepository = accountRepository;
        this.paymentHistoryRepository = paymentHistoryRepository;
        this.transactHistoryRepository = transactHistoryRepository;
    }

    @Override
    public ResponseEntity<?> getDashboard(User user) {
        try {
            int userId = Integer.parseInt(user.getUser_id());
            List<Account> userAccounts = accountRepository.getUserAccountsById(userId);
            BigDecimal totalAccountsBalance = accountRepository.getTotalBalance(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("userAccounts", userAccounts);
            response.put("totalBalance", totalAccountsBalance);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching dashboard data: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getPaymentHistory(User user) {
        try {
            int userId = Integer.parseInt(user.getUser_id());
            List<PaymentHistory> userPaymentHistory = paymentHistoryRepository.getPaymentsRecordsById(userId);

            Map<String, List> response = new HashMap<>();
            response.put("payment_history", userPaymentHistory);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching payment history: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getTransactionHistory(User user) {
        try {
            int userId = Integer.parseInt(user.getUser_id());
            List<TransactionHistory> userTransactionHistory = transactHistoryRepository.getTransactionRecordsById(userId);

            Map<String, List> response = new HashMap<>();
            response.put("transaction_history", userTransactionHistory);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching transaction history: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAccountTransactionHistory(Map<String, String> requestMap) {
        try {
            String account_id = requestMap.get("account_id");
            int accountId = Integer.parseInt(account_id);

            List<TransactionHistory> accountTransactionHistory = transactHistoryRepository.getTransactionRecordsByAccountId(accountId);

            Map<String, List> response = new HashMap<>();
            response.put("transaction_history", accountTransactionHistory);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching account transaction history: " + e.getMessage());
        }
    }
}
