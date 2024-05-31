package online.jeweljoust.BE.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter

public class RegisterRequest {
    String username;
    String password;
    String fullname;
    String address;
    Date birthday;
    String email;
    String phone;
}
