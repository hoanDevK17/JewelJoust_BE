package online.jeweljoust.BE.utils;

import online.jeweljoust.BE.entity.Account;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component

public class AccountUtils {
    public Account getAccountCurrent(){
        return (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public boolean isLoggedIn() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         return principal instanceof UserDetails;
    }
}
