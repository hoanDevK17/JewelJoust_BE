package online.jeweljoust.BE.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionBidRequest {
    long registration_id;
    double price;
}
