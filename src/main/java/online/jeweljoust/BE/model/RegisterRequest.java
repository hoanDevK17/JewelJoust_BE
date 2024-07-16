package online.jeweljoust.BE.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import online.jeweljoust.BE.enums.AccountRole;
import online.jeweljoust.BE.enums.AccountStatus;

import java.util.Date;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RegisterRequest {
    String username;
    String password;
    String fullname;
    String address;
    Date birthday;
    String email;
    String phone;
    AccountRole role;
    String status;
}
