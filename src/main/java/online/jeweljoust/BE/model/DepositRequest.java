package online.jeweljoust.BE.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class DepositRequest {
    private Long walletId;
    private Double amount;
    private String description;
}
