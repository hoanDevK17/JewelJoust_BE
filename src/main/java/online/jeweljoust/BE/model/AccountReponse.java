package online.jeweljoust.BE.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import online.jeweljoust.BE.entity.Account;
import org.springframework.stereotype.Service;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountReponse extends Account {
    String token;
}
