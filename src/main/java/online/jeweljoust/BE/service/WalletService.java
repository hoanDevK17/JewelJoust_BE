package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.Wallet;
import online.jeweljoust.BE.respository.AuthenticationRepository;
import online.jeweljoust.BE.respository.WalletRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class WalletService {
        @Autowired
        WalletRepository walletRepository;
        @Autowired
        AccountUtils accountUtils;
        @Autowired
        AuthenticationRepository authenticationRepository;
        public Wallet registerWallet(Account account){
        Wallet wallet = new Wallet();
        wallet.setBalance(0.0);
        wallet.setUpdateAt(new Date());
        wallet.setCreateAt(new Date());
        wallet.setAccountWallet(account);
//        Wallet resposeWallet = walletRepository.save(wallet);
//        account.setWallet(resposeWallet);
        

//        wallet.setAccountWallet(account);
        return walletRepository.save(wallet);
    }
        public Wallet changBalance(Long id,double amount){
                Wallet wallet = walletRepository.findWalletById(id);
                wallet.setBalance(wallet.getBalance()+amount);
                return walletRepository.save(wallet);
        }
        public double refreshBalance(){
                Wallet wallet = accountUtils.getAccountCurrent().getWallet();
                return wallet.getBalance();
        }
}
