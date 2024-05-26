package online.jeweljoust.BE.respository;


import online.jeweljoust.BE.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<Account, Long>
{   // dua ra daatabase
    Account findAccountByPhone(String phone);

}
