package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.model.EmailDetail;
import online.jeweljoust.BE.model.LoginRequest;
import online.jeweljoust.BE.model.RegisterRequest;
import online.jeweljoust.BE.service.AuthenticationService;
import online.jeweljoust.BE.service.EmailService;
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
    EmailService emailService;

    // @GetMapping("/product/{id}")
    // public ResponseEntity test(@PathVariable String id){
    // return ResponseEntity.ok("Hello world");
    // http://localhost:8080/api/product/123312312
    // }
    // @GetMapping("/product")
    // public ResponseEntity test(@RequestParam("ID") String id ){
    // return ResponseEntity.ok("Hello world");
    // }
    // http://localhost:8080/api/product?id=1233123
    // @PostMapping("/product")
    // public ResponseEntity test321(){
    // return ResponseEntity.ok("Hello world");
    // }
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest) {
        Account account = authenticationService.register(registerRequest);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/registerManager")
    public ResponseEntity registerManagement(@RequestBody RegisterRequest registerRequest) {
        try {
            Account account = authenticationService.registerManager(registerRequest);
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

    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        Account account = authenticationService.login(loginRequest);
        return ResponseEntity.ok(account);
    }

    @GetMapping("send-mail")
    public void sendMail() {
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient("phatttse170312@fpt.edu.vn");
        emailDetail.setSubject("test123");
        emailDetail.setMsgBody("abc");
        emailService.sendMailTemplate(emailDetail);
    }
}
