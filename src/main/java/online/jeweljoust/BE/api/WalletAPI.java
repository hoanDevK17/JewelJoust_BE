package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.Transaction;
import online.jeweljoust.BE.enums.TransactionType;
import online.jeweljoust.BE.model.DepositRequest;
import online.jeweljoust.BE.model.RechargeRequestDTO;
import online.jeweljoust.BE.service.TransactionService;
import online.jeweljoust.BE.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "api")
@RequestMapping("api")
@CrossOrigin("*")
public class WalletAPI {
    @Autowired
    WalletService walletService;

    @PostMapping("/wallet/createUrl")
    public ResponseEntity create(@RequestBody RechargeRequestDTO rechargeRequestDTO) throws Exception {
        String url = walletService.createUrl(rechargeRequestDTO);
        return ResponseEntity.ok(url);
    }
    @PostMapping("/wallet/deposit")
    public ResponseEntity deposit( @RequestBody DepositRequest depositRequest)  {
        Transaction transaction = walletService.deposit( depositRequest);
        return ResponseEntity.ok(transaction);
    }
    @GetMapping("/wallet/transaction-history")
    public ResponseEntity getTransactionHistory()  {
        List<Transaction> transactions = walletService.getTransactionHistory();
        return ResponseEntity.ok(transactions);
    }
    @GetMapping("/wallet/activity-history")
    public ResponseEntity getWalletActivityHistory()  {
        List<Transaction> transactions = walletService.getWalletActivityHistory();
        return ResponseEntity.ok(transactions);
    }

}
