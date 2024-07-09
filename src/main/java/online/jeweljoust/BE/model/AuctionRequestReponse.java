package online.jeweljoust.BE.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Getter
@Setter

public class AuctionRequestReponse {
    String jewelryName;
    String jewelryDescription;
    double initialPrice;
    List<ResourceRequest> resourceRequests;
}