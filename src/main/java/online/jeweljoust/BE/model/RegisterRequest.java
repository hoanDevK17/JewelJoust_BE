package online.jeweljoust.BE.model;

import lombok.Data;

import java.util.Date;

@Data
public class RegisterRequest {
    String username;
    String password;
    String fullname;
    String address;
    Date birthday;
    String email;
    String phone;
    final String role = "Member";
    final int credibility = 0;

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public String getFullname(){
        return this.fullname;
    }

    public String getAddress(){
        return this.address;
    }

    public Date getBirthday(){
        return this.birthday;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPhone(){
        return this.phone;
    }

}
