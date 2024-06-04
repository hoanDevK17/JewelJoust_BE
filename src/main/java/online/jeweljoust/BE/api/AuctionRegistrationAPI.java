package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.model.AuctionSaleReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")

public class AuctionRegistrationAPI {
//    @PostMapping("/auction-registrations")
//    public ResponseEntity create_auction_registration(@RequestBody AuctionSaleReponse auctionSaleReponse) {
//        AuctionRequest auctionRequest = auctionSaleService.requestSale(auctionSaleReponse);
//        return ResponseEntity.ok(auctionRequest);
//    }

}
