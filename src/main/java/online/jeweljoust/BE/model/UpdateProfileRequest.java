package online.jeweljoust.BE.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

public class UpdateProfileRequest {
    long userid;
    String username;
    String password;
    String fullname;
    String address;
    Date birthday;
    String email;
    String phone;
}
