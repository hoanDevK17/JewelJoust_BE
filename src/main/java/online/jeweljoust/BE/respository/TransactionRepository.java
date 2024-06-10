package online.jeweljoust.BE.respository;


import online.jeweljoust.BE.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
