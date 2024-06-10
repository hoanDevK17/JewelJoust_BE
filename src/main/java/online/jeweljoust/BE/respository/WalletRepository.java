package online.jeweljoust.BE.respository;


import online.jeweljoust.BE.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository   extends JpaRepository<Wallet, Long> {
//    Wallet findWalletByUserid(Long userID);
    Wallet findWalletById(long id);
}
