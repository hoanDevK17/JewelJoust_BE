package online.jeweljoust.BE.respository;

import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.enums.AccountRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuthenticationRepository extends JpaRepository<Account, Long>
{
    Account findByUsername(String username);
    Account findByEmail(String email);
    Account findById(long userid);
    Account findAccountById(long userid);
    List<Account> findByFullnameContaining(String name);
    List<Account> findAccountByRole(AccountRole role);
    @Query("SELECT a FROM Account a")
    Page<Account> findAllAccounts(Pageable pageable);

    @Query("SELECT COUNT(a) FROM Account a WHERE a.role = 'MEMBER'")
    long countTotalAccounts();

    @Query(value = "SELECT MONTH(w.create_at) AS month, COUNT(a.id) AS memberCount " +
            "FROM account a " +
            "JOIN wallet w ON a.id = w.account_id " +
            "WHERE a.role = 'MEMBER' AND YEAR(w.create_at) = :year " +
            "GROUP BY MONTH(w.create_at) " +
            "ORDER BY month", nativeQuery = true)
    List<Object[]> countAccountsByMonth(long year);
}
