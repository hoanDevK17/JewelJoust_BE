package online.jeweljoust.BE.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter

public class WithdrawRequest {
    private String bankName;
    private String accountNumber;
    private String recipientName;
    private double amountWithDraw;
    private double usd;
}
