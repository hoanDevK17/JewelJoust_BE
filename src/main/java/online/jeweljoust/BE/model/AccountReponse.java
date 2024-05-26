package online.jeweljoust.BE.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import online.jeweljoust.BE.entity.Account;


@Getter
@Setter
public class AccountReponse extends Account {
    String token;
}
