package online.jeweljoust.BE.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Data
@Service
@Getter
@Setter

public class AuctionSaleReponse {
    String jewelryName;
    String jewelryDescription;
    double initialPrice;
}