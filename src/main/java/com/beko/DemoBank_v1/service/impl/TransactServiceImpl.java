package com.beko.DemoBank_v1.service.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
public class TransactServiceImpl implements TransactService {

    private final AccountRepository accountRepository;
    private final TransactRepository transactRepository;

    @Autowired
    public TransactServiceImpl(AccountRepository accountRepository, TransactRepository transactRepository) {
        this.accountRepository = accountRepository;
        this.transactRepository = transactRepository;
    }

    public ResponseEntity deposit(Map<String, String> requestMap, User user) {
        try {
            validateDepositRequest(requestMap);

            int accountId = Integer.parseInt(requestMap.get("account_id"));
            double depositAmount = Double.parseDouble(requestMap.get("deposit_amount"));
            int userId = Integer.parseInt(user.getUser_id());

            double currentBalance = accountRepository.getAccountBalance(userId, accountId);
            double newBalance = currentBalance + depositAmount;

            accountRepository.changeAccountsBalanceById(newBalance, accountId);

            transactRepository.logTransaction(accountId, "deposit", depositAmount, "online", "success", "Deposit Transaction Successful", LocalDateTime.now());

            return ResponseEntity.ok(buildDepositResponse(userId));

        } catch (Exception e) {
            return handleException(e);
        }
    }

    public ResponseEntity payment(PaymentRequest request, User user) {
        try {
            validatePaymentRequest(request);

            int accountId = Integer.parseInt(request.getAccount_id());
            double paymentAmount = Double.parseDouble(request.getPayment_amount());
            int userId = Integer.parseInt(user.getUser_id());

            double currentBalance = accountRepository.getAccountBalance(userId, accountId);

            if (currentBalance < paymentAmount) {
                handleInsufficientFunds(accountId);
                return ResponseEntity.badRequest().body("You have insufficient funds to perform this payment.");
            }

            double newBalance = currentBalance - paymentAmount;
            accountRepository.changeAccountsBalanceById(newBalance, accountId);

            transactRepository.logTransaction(accountId, "Payment", paymentAmount, "online", "success", "Payment Transaction Successful", LocalDateTime.now());

            return ResponseEntity.ok(buildPaymentResponse(userId));

        } catch (Exception e) {
            return handleException(e);
        }
    }

    public ResponseEntity withdraw(Map<String, String> requestMap, User user) {
        try {
            validateWithdrawalRequest(requestMap);

            int accountId = Integer.parseInt(requestMap.get("account_id"));
            double withdrawalAmount = Double.parseDouble(requestMap.get("withdrawal_amount"));
            int userId = Integer.parseInt(user.getUser_id());

            double currentBalance = accountRepository.getAccountBalance(userId, accountId);

            if (currentBalance < withdrawalAmount) {
                handleInsufficientFunds(accountId);
                return ResponseEntity.badRequest().body("You have insufficient funds to perform this withdrawal.");
            }

            double newBalance = currentBalance - withdrawalAmount;
            accountRepository.changeAccountsBalanceById(newBalance, accountId);

            transactRepository.logTransaction(accountId, "Withdrawal", withdrawalAmount, "online", "success", "Withdrawal Transaction Successful", LocalDateTime.now());

            return ResponseEntity.ok(buildWithdrawalResponse(userId));

        } catch (Exception e) {
            return handleException(e);
        }
    }

    public ResponseEntity transfer(TransferRequest request, User user) {
        try {
            validateTransferRequest(request);

            int sourceAccountId = Integer.parseInt(request.getSourceAccount());
            int targetAccountId = Integer.parseInt(request.getTargetAccount());
            double transferAmount = Double.parseDouble(request.getAmount());
            int userId = Integer.parseInt(user.getUser_id());

            if (sourceAccountId == targetAccountId) {
                return ResponseEntity.badRequest().body("Cannot transfer into the same account. Please select the appropriate account to perform the transfer.");
            }

            double sourceBalance = accountRepository.getAccountBalance(userId, sourceAccountId);

            if (sourceBalance < transferAmount) {
                handleInsufficientFunds(sourceAccountId);
                return ResponseEntity.badRequest().body("You have insufficient funds to perform this transfer.");
            }

            double newSourceBalance = sourceBalance - transferAmount;
            double targetBalance = accountRepository.getAccountBalance(userId, targetAccountId);
            double newTargetBalance = targetBalance + transferAmount;

            accountRepository.changeAccountsBalanceById(newSourceBalance, sourceAccountId);
            accountRepository.changeAccountsBalanceById(newTargetBalance, targetAccountId);

            transactRepository.logTransaction(sourceAccountId, "Transfer", transferAmount, "online", "success", "Transfer Transaction Successful", LocalDateTime.now());

            return ResponseEntity.ok(buildTransferResponse(userId));

        } catch (Exception e) {
            return handleException(e);
        }
    }

    private void validateDepositRequest(Map<String, String> requestMap) {
        String depositAmount = requestMap.get("deposit_amount");
        String accountID = requestMap.get("account_id");

        if (StringUtils.isEmpty(depositAmount) || StringUtils.isEmpty(accountID)) {
            throw new IllegalArgumentException("Deposit amount and account ID cannot be empty.");
        }

        int acc_id = Integer.parseInt(accountID);
        double depositAmountValue = Double.parseDouble(depositAmount);

        if (depositAmountValue == 0) {
            throw new IllegalArgumentException("Deposit amount cannot be zero.");
        }
    }


    private void validatePaymentRequest(PaymentRequest request) {
        String beneficiary = request.getBeneficiary();
        String accountNumber = request.getAccount_number();
        String accountID = request.getAccount_id();
        String reference = request.getReference();
        String paymentAmount = request.getPayment_amount();

        if (StringUtils.isEmpty(beneficiary) || StringUtils.isEmpty(accountNumber) || StringUtils.isEmpty(accountID) || StringUtils.isEmpty(paymentAmount)) {
            throw new IllegalArgumentException("Beneficiary, account number, account paying from, and account payment amount cannot be empty.");
        }

        int accountId = Integer.parseInt(accountID);
        double paymentAmountValue = Double.parseDouble(paymentAmount);

        if (paymentAmountValue == 0) {
            throw new IllegalArgumentException("Payment amount cannot be zero.");
        }
    }


    private void validateWithdrawalRequest(Map<String, String> requestMap) {
        String withdrawalAmount = requestMap.get("withdrawal_amount");
        String accountId = requestMap.get("account_id");

        if (StringUtils.isEmpty(withdrawalAmount) || StringUtils.isEmpty(accountId)) {
            throw new IllegalArgumentException("Account withdrawing from and withdrawal amount cannot be empty!");
        }

        int account_id = Integer.parseInt(accountId);
        double withdrawal_amount = Double.parseDouble(withdrawalAmount);

        if (withdrawal_amount == 0) {
            throw new IllegalArgumentException("Withdrawal amount cannot be 0 value.");
        }
    }

    private void validateTransferRequest(TransferRequest request) {
        String transferFrom = request.getSourceAccount();
        String transferTo = request.getTargetAccount();
        String transferAmount = request.getAmount();

        if (StringUtils.isEmpty(transferFrom) || StringUtils.isEmpty(transferTo) || StringUtils.isEmpty(transferAmount)) {
            throw new IllegalArgumentException("The account transferring from, to, and the amount cannot be empty!");
        }

        int transferFromId = Integer.parseInt(transferFrom);
        int transferToId = Integer.parseInt(transferTo);
        double transferAmountValue = Double.parseDouble(transferAmount);

        if (transferFromId == transferToId) {
            throw new IllegalArgumentException("Cannot transfer into the same account. Please select the appropriate account to perform the transfer.");
        }

        if (transferAmountValue == 0) {
            throw new IllegalArgumentException("Cannot transfer an amount of 0 (zero) value. Please enter a value greater than zero.");
        }
    }


    private void handleInsufficientFunds(int accountId) {
        transactRepository.logTransaction(accountId, "withdrawal", 0.0, "online", "failed", "Insufficient funds.", LocalDateTime.now());
    }

    private ResponseEntity handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
    }

    private Map<String, Object> buildDepositResponse(int userId) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Amount Deposited Successfully.");
        response.put("accounts", accountRepository.getUserAccountsById(userId));
        return response;
    }

    private Map<String, Object> buildPaymentResponse(int userId) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Payment Processed Successfully!");
        response.put("accounts", accountRepository.getUserAccountsById(userId));
        return response;
    }

    private Map<String, Object> buildWithdrawalResponse(int userId) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Withdrawal Successful!");
        response.put("accounts", accountRepository.getUserAccountsById(userId));
        return response;
    }

    private Map<String, Object> buildTransferResponse(int userId) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Transfer Completed Successfully.");
        response.put("accounts", accountRepository.getUserAccountsById(userId));
        return response;
    }
}
