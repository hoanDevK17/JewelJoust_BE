package online.jeweljoust.BE.model;

import lombok.Getter;
import lombok.Setter;
import online.jeweljoust.BE.enums.AuctionConfirmStatus;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter

public class MemberConfirmRequest {
    long id;
    AuctionConfirmStatus status;
}
