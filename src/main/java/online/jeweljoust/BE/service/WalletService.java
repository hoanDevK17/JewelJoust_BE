package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.Wallet;
import online.jeweljoust.BE.respository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class WalletService {
        @Autowired
        WalletRepository walletRepository;
        public Wallet registerWallet(Account account){
        Wallet wallet = new Wallet();
        wallet.setBalance(0.0);
        wallet.setUpdateAt(new Date());
        wallet.setCreateAt(new Date());
        wallet.setId(account.getId());
//        wallet.setAccountWallet(account);
        return walletRepository.save(wallet);
    }
}
