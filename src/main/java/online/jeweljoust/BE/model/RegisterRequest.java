package online.jeweljoust.BE.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    String username;
    String password;
    String fullname;
    String address;
    Date birthday;
    String email;
    String phone;
    String role;
}
