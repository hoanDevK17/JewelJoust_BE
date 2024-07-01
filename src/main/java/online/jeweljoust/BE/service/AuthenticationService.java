package online.jeweljoust.BE.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import online.jeweljoust.BE.config.SecurityConfig;
import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.enums.AccountRole;
import online.jeweljoust.BE.enums.AccountStatus;
import online.jeweljoust.BE.exception.AuthException;
import online.jeweljoust.BE.model.*;
import online.jeweljoust.BE.respository.AuthenticationRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenService tokenService;

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    SecurityConfig securityConfig;



    @Autowired
    AccountUtils accountUtils;

    @Autowired
    EmailService emailService;

    @Autowired
    WalletService walletService;

    public Account register(RegisterRequest registerRequest){
        Account account = new Account();
        account.setUsername(registerRequest.getUsername());
        account.setFullname(registerRequest.getFullname());
        account.setAddress(registerRequest.getAddress());
        account.setBirthday(registerRequest.getBirthday());
        account.setEmail(registerRequest.getEmail());
        account.setPhone(registerRequest.getPhone());
        account.setStatus(AccountStatus.ACTIVE);
        account.setRole(AccountRole.MEMBER);
        account.setCredibility(0);
        account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return authenticationRepository.save(account);
    }

    public Account registerHaveRole(RegisterRequest registerRequest) throws AuthenticationServiceException{
        Account account = new Account();
            account.setRole(registerRequest.getRole());
            account.setUsername(registerRequest.getUsername());
            account.setFullname(registerRequest.getFullname());
            account.setAddress(registerRequest.getAddress());
            account.setBirthday(registerRequest.getBirthday());
            account.setEmail(registerRequest.getEmail());
            account.setPhone(registerRequest.getPhone());
            account.setStatus(AccountStatus.ACTIVE);
            account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return authenticationRepository.save(account);
    }

    public AccountReponse login(LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            ));
            Account account = authenticationRepository.findByUsername(loginRequest.getUsername());
            if (account == null || !securityConfig.passwordEncoder().matches(loginRequest.getPassword(), account.getPassword())) {
                throw new BadCredentialsException("Incorrect username or password");
            }
            AccountReponse accountReponse = new AccountReponse();
            String token = tokenService.generateToken(account);
            accountReponse.setUsername(account.getUsername());
            accountReponse.setId(account.getId());
            accountReponse.setFullname(account.getFullname());
            accountReponse.setAddress(account.getAddress());
            accountReponse.setBirthday(account.getBirthday());
            accountReponse.setEmail(account.getEmail());
            accountReponse.setPhone(account.getPhone());
            accountReponse.setRole(account.getRole());
            accountReponse.setCredibility(account.getCredibility());
            accountReponse.setStatus(account.getStatus());
            accountReponse.setToken(token);
            accountReponse.setWallet(account.getWallet());
            return accountReponse;
        } catch (AuthenticationException e){
            throw new BadCredentialsException("Incorrect username or password");
        }
    }

    public AccountReponse loginGoogle(LoginGoogleRequest loginGoogleRequest){
        AccountReponse accountReponse = new AccountReponse();
        try {
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(loginGoogleRequest.getToken());
            String email = firebaseToken.getEmail();
            System.out.println(email);
            Account account = authenticationRepository.findByEmail(email);
            System.out.println(account.getId());
            if (account == null){
                account = new Account();
                account.setFullname(firebaseToken.getName());
                account.setEmail(firebaseToken.getEmail());
                account.setRole(AccountRole.MEMBER);
                account.setStatus(AccountStatus.ACTIVE);
                account.setUsername(email);
                account = authenticationRepository.save(account);
                if(account.getId()!=null){
                    account.setWallet(walletService.registerWallet(account));
                }
            }
            String token = tokenService.generateToken(account);
            accountReponse.setUsername(account.getUsername());
            accountReponse.setId(account.getId());
            accountReponse.setFullname(account.getFullname());
            accountReponse.setAddress(account.getAddress());
            accountReponse.setBirthday(account.getBirthday());
            accountReponse.setEmail(account.getEmail());
            accountReponse.setPhone(account.getPhone());
            accountReponse.setRole(account.getRole());
            accountReponse.setCredibility(account.getCredibility());
            accountReponse.setStatus(account.getStatus());
            accountReponse.setToken(token);
            accountReponse.setWallet(account.getWallet());
        } catch (Exception e){
            e.getMessage();
        }
        return accountReponse;
    }

    public List<Account> getAllAccount(){
        return authenticationRepository.findAll();
    }

    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        Account account =  authenticationRepository.findByEmail(forgotPasswordRequest.getEmail());
        if (account == null){
            try {
                throw new BadRequestException("Account not found!!!");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient(account.getEmail());
        emailDetail.setFullName(accountUtils.getAccountCurrent().getFullname());
        emailDetail.setLink("jeweljoust.online");
        emailDetail.setSubject("Reset password for account " + forgotPasswordRequest.getEmail() + "!");
        emailDetail.setMsgBody("alo");
        emailDetail.setButtonValue("Reset password");
        String token = tokenService.generateToken(account);
        System.out.println(token);
        emailDetail.setLink("http://jeweljoust.online/reset-password?token=" + token);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                emailService.sendMailTemplate(emailDetail);
            }
        };
        new Thread(runnable).start();
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
            Account account = accountUtils.getAccountCurrent();
            account.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
            authenticationRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authenticationRepository.findByUsername(username);
    }

    public Account updateProfile(UpdateProfileRequest updateProfileRequest) {
        if(accountUtils.getAccountCurrent().getRole().equals(AccountRole.ADMIN)||accountUtils.getAccountCurrent().getId() == updateProfileRequest.getId())
        {
            Account account = authenticationRepository.findById(updateProfileRequest.getId());
            if (updateProfileRequest.getFullname() != null) {
                account.setFullname(updateProfileRequest.getFullname());
            }
            if (updateProfileRequest.getAddress() != null) {
                account.setAddress(updateProfileRequest.getAddress());
            }
            if (updateProfileRequest.getBirthday() != null) {
                account.setBirthday(updateProfileRequest.getBirthday());
            }
            if (updateProfileRequest.getEmail() != null) {
                account.setEmail(updateProfileRequest.getEmail());
            }
            if (updateProfileRequest.getPhone() != null) {
                account.setPhone(updateProfileRequest.getPhone());
                if(accountUtils.getAccountCurrent().getRole().equals(AccountRole.ADMIN)){
                        account.setStatus(updateProfileRequest.getStatus());
                }
            }
            return authenticationRepository.save(account);
        }
        else{
            throw new AuthException("Can't access to edit");
        }

    }

    public List<Account> getAccountByName(String name) {
        return authenticationRepository.findByFullnameContaining(name) ;
    }

    public void blockAccount(long id, AccountStatus status) {
        Account account = authenticationRepository.findById(id);
        account.setStatus(status);
        authenticationRepository.save(account);
    }

    public void deleteAccountById(long id) {
        authenticationRepository.deleteById(id);
    }

    public String changePassword(ChangePasswordRequest changePasswordRequest) {
        Account account = authenticationRepository.findByUsername(accountUtils.getAccountCurrent().getUsername());
        if (securityConfig.passwordEncoder().matches(changePasswordRequest.getOldPassword(), account.getPassword())) {
            account.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            authenticationRepository.save(account);
            return "Change password Succesfully";
        }
        else{
            return "Changed password not successfully";
        }
    }
}
