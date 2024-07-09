package online.jeweljoust.BE.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import online.jeweljoust.BE.entity.AuctionSession;


@Getter
@Setter
public class AuctionSessionDetailResponse extends AuctionSession {
    boolean isRegister;

}
