package online.jeweljoust.BE.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RejectUltimateRequest {
    long id_auctionRequest;
    String reason;
}
