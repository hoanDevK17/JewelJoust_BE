package online.jeweljoust.BE.respository;


import online.jeweljoust.BE.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthenticationRepository extends JpaRepository<Account, Long>
{
    Account findAccountByUsername(String username);
    Account findAccountByEmail(String email);
    Account findAccountByUserid(long userid);
    List<Account> findAccountByFullnameContaining(String name);
}
