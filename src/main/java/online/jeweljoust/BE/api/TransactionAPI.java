package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.Transaction;
import online.jeweljoust.BE.service.TransactionService;
import online.jeweljoust.BE.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@SecurityRequirement(name = "api")
@RequestMapping("api")
public class TransactionAPI {
    @Autowired
    TransactionService transactionService;
    @GetMapping("/transactions")
    public ResponseEntity getAll()  {
        List<Transaction> transactions = transactionService.getAll();
        return ResponseEntity.ok(transactions);
    }
}
