package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.Account;
//import online.jeweljoust.BE.entity.Wallet;
import online.jeweljoust.BE.entity.Wallet;
import online.jeweljoust.BE.model.*;
import online.jeweljoust.BE.service.AuthenticationService;
import online.jeweljoust.BE.service.EmailService;
//import online.jeweljoust.BE.service.WalletService;
import online.jeweljoust.BE.service.WalletService;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")

public class AuthenticationAPI {
    // Nhan request tu FE
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    WalletService walletService;

    @Autowired
    EmailService emailService;

    @Autowired
    AccountUtils accountUtils;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest) {
        Account account = authenticationService.register(registerRequest);
        if(account.getUserid()>0){
            Wallet wallet = walletService.registerWallet(account);
            System.out.println(wallet);
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping("/registerHaveRole")
    public ResponseEntity registerHaveRole(@RequestBody RegisterRequest registerRequest) {
        try {
            Account account = authenticationService.registerHaveRole(registerRequest);
            return ResponseEntity.ok(account);
        } catch (AuthenticationServiceException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        List<Account> accounts = authenticationService.getAllAccount();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/testcurrent")
    public Account current() {
        return accountUtils.getAccountCurrent();
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        Account account = authenticationService.login(loginRequest);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/login-google")
    public ResponseEntity<AccountReponse> loginGoogle(@RequestBody LoginGoogleRequest loginGoogleRequest) {
        AccountReponse accountReponse = authenticationService.loginGoogle(loginGoogleRequest);
        if(accountReponse.getUserid()>0){
            Wallet wallet = walletService.registerWallet(accountReponse);
            System.out.println(wallet);
        }
        return ResponseEntity.ok(accountReponse);
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        authenticationService.forgotPassword(forgotPasswordRequest);
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        authenticationService.resetPassword(resetPasswordRequest);
    }

//    @PutMapping("/update-profile")
    

    @GetMapping("send-mail")
    public void sendMail() {
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient("phatttse170312@fpt.edu.vn");
        emailDetail.setSubject("test123");
        emailDetail.setMsgBody("abc");
        emailService.sendMailTemplate(emailDetail);
    }
}
