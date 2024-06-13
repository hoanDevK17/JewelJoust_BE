package online.jeweljoust.BE.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import online.jeweljoust.BE.enums.AuctionRequestStatus;

@Data
@Getter
@Setter

public class InitialRequest {
    String reason;
    double price;
}
