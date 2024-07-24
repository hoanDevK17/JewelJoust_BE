package online.jeweljoust.BE.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import online.jeweljoust.BE.entity.AuctionBid;
import online.jeweljoust.BE.entity.AuctionSession;

import java.util.List;


@Getter
@Setter
public class AuctionSessionDetailResponse extends AuctionSession {
    boolean isRegister;

    List<AuctionBid> three_highestBid;
    AuctionBid highestBid;

}
