package online.jeweljoust.BE.respository;


import online.jeweljoust.BE.entity.Transaction;
import online.jeweljoust.BE.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Transaction> findByWalletId (long id, Pageable pageable);
    @Query("SELECT t FROM Transaction t WHERE t.transaction_type = 'WITHDRAW'")
    List<Transaction> findAllWithDrawRequest ();
    Transaction findByTxnRef(String tsnRef);
    Transaction findTransactionById(long id);
}
