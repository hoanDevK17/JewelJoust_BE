package online.jeweljoust.BE.respository;


import online.jeweljoust.BE.entity.Transaction;
import online.jeweljoust.BE.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.wallet.id = :walletId AND t.transaction_type  IN (:types)")
    List<Transaction> findWalletActivityByWalletId(@Param("walletId") Long walletId, @Param("types") List<TransactionType> types);
    @Query("SELECT t FROM Transaction t WHERE t.wallet.id = :walletId AND t.transaction_type NOT IN (:types)")
    List<Transaction> findTransactionHistoryByWalletId(@Param("walletId")Long walletId ,@Param("types") List<TransactionType> types);
    List<Transaction> findByWalletId (long id);
}
