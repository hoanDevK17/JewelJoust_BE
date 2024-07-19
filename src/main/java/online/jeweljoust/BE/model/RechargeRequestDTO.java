package online.jeweljoust.BE.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RechargeRequestDTO {
    String amount;
    String usd;
}
