package online.jeweljoust.BE.model;

import lombok.Data;

@Data
public class LoginRequest {
    String phone;
    String password;

    public String getPhone(){
        return this.phone;
    }

    public String getPassword(){
        return this.password;
    }
}
