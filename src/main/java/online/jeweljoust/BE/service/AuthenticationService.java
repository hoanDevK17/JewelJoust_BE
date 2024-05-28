package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.model.AccountReponse;
import online.jeweljoust.BE.model.LoginRequest;
import online.jeweljoust.BE.model.RegisterRequest;
import online.jeweljoust.BE.respository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenService tokenService;

    @Autowired
    AuthenticationRepository authenticationRepository;
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
        //nh REPO ==> SAVE xun db
        return authenticationRepository.save(account);
    }
    public AccountReponse login(LoginRequest loginRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getPhone(),
                loginRequest.getPassword()
        ));
        //===> chuẩn
        Account account = authenticationRepository.findAccountByPhone(loginRequest.getPhone());
        String token = tokenService.generateToken(account);
        AccountReponse accountReponse = new AccountReponse();
        accountReponse.setPhone(account.getPhone());
        accountReponse.setToken(token);
        return accountReponse;
    }

    public List<Account> getAllAccount(){
        return authenticationRepository.findAll();
    }
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        return authenticationRepository.findAccountByPhone(phone);
    }
}
