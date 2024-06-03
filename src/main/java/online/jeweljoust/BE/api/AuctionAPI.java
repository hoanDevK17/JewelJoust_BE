package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.model.AuctionSaleReponse;
import online.jeweljoust.BE.service.AuctionSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")

public class AuctionAPI {

    @Autowired
    AuctionSaleService auctionSaleService;

    @PostMapping("/request-sale")
    public ResponseEntity requestSaleAuction(@RequestBody AuctionSaleReponse auctionSaleReponse) {
        AuctionRequest auctionRequest = auctionSaleService.requestSale(auctionSaleReponse);
        return ResponseEntity.ok(auctionRequest);
    }

}
