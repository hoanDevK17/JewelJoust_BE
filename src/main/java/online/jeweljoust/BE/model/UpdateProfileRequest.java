package online.jeweljoust.BE.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import online.jeweljoust.BE.enums.AccountStatus;

import java.util.Date;
@Data
@Getter
@Setter
public class UpdateProfileRequest {
    long id;
    String fullname;
    String address;
    Date birthday;
    String email;
    String phone;
    AccountStatus status;

}
