package online.jeweljoust.BE.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;


@Service
@Getter
@Setter
public class ChangePasswordRequest
{
    String oldPassword;
    String newPassword;
}
