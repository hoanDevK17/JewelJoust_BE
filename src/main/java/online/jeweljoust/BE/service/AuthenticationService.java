package online.jeweljoust.BE.service;

import online.jeweljoust.BE.config.SecurityConfig;
import online.jeweljoust.BE.entity.Account;
//import online.jeweljoust.BE.entity.Wallet;
import online.jeweljoust.BE.exception.APIHandleException;
import online.jeweljoust.BE.model.AccountReponse;
import online.jeweljoust.BE.model.LoginRequest;
import online.jeweljoust.BE.model.RegisterRequest;
import online.jeweljoust.BE.respository.AuthenticationRepository;
//import online.jeweljoust.BE.respository.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
//    @Autowired
//    WalletRepository walletRepository;

    @Autowired
    SecurityConfig securityConfig;

    @Autowired
    AccountReponse accountReponse;

    //xu ly logic
    public Account register(RegisterRequest registerRequest){
        //xu ly logic register
        Account account = new Account();
        account.setUsername(registerRequest.getUsername());
        account.setFullname(registerRequest.getFullname());
        account.setAddress(registerRequest.getAddress());
        account.setBirthday(registerRequest.getBirthday());
        account.setEmail(registerRequest.getEmail());
        account.setPhone(registerRequest.getPhone());
        account.setRole("Member");
        account.setCredibility(0);
        account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return authenticationRepository.save(account);
    }

    public AccountReponse login(LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            ));
            Account account = authenticationRepository.findAccountByUsername(loginRequest.getUsername());
            if (account == null || !securityConfig.passwordEncoder().matches(loginRequest.getPassword(), account.getPassword())) {
                throw new BadCredentialsException("Incorrect username or password");
            }
                AccountReponse accountReponse = new AccountReponse();
                String token = tokenService.generateToken(account);
                accountReponse.setUsername(account.getUsername());
                accountReponse.setUserid(account.getUserid());
                accountReponse.setFullname(account.getFullname());
                accountReponse.setAddress(account.getAddress());
                accountReponse.setBirthday(account.getBirthday());
                accountReponse.setEmail(account.getEmail());
                accountReponse.setPhone(account.getPhone());
                accountReponse.setRole(account.getRole());
                accountReponse.setCredibility(account.getCredibility());
                accountReponse.setToken(token);
                return accountReponse;
        } catch (AuthenticationException e){
            throw new BadCredentialsException("Incorrect username or password");
        }
    }

    public Account registerManager(RegisterRequest registerRequest) throws AuthenticationServiceException{
        String token = accountReponse.getToken();
        try {
            if ("Admin".equals(tokenService.extractAccount(token).getRole())){
                Account account = new Account();
                account.setUsername(registerRequest.getUsername());
                account.setFullname(registerRequest.getFullname());
                account.setAddress(registerRequest.getAddress());
                account.setBirthday(registerRequest.getBirthday());
                account.setEmail(registerRequest.getEmail());
                account.setPhone(registerRequest.getPhone());
                account.setRole("Manager");
                account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
                return authenticationRepository.save(account);
            } else {
                throw new AuthenticationServiceException("Your role can't register manager!!!");
            }
        } catch (Exception e){
            throw new AuthenticationServiceException("Error at register manager");
        }

    }

    public List<Account> getAllAccount(){
        return authenticationRepository.findAll();
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authenticationRepository.findAccountByUsername(username);
    }
}
