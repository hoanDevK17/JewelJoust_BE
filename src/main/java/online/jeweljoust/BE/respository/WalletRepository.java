package online.jeweljoust.BE.respository;


import online.jeweljoust.BE.entity.Transaction;
import online.jeweljoust.BE.entity.Wallet;
import online.jeweljoust.BE.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WalletRepository   extends JpaRepository<Wallet, Long> {
//    Wallet findWalletByUserid(Long userID);
    Wallet findWalletById(long id);
    Wallet findWallelByAccountWalletId(long id);
}
