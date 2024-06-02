package online.jeweljoust.BE.utils;

import online.jeweljoust.BE.entity.Account;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component

public class AccountUtils {
    public Account getAccountCurrent(){
        return (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
