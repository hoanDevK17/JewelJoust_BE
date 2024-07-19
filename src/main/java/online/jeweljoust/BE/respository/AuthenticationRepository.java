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

    @Query("SELECT COUNT(a) FROM Account a")
    long countTotalAccounts();
}
