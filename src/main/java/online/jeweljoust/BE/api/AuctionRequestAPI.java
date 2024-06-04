package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.model.AuctionSaleReponse;
import online.jeweljoust.BE.model.UpdateProfileRequest;
import online.jeweljoust.BE.service.AuctionSaleService;
import org.apache.http.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")

public class AuctionRequestAPI {

    @Autowired
    AuctionSaleService auctionSaleService;

    @PostMapping("/request-sale")
    public ResponseEntity requestSaleAuction(@RequestBody AuctionSaleReponse auctionSaleReponse) {
        AuctionRequest auctionRequest = auctionSaleService.requestSale(auctionSaleReponse);
        return ResponseEntity.ok(auctionRequest);
    }

    @GetMapping("/auction-request-by-userid")
    public ResponseEntity<List<AuctionRequest>> getAuctionByUserid() {
        List<AuctionRequest> auctionRequests = auctionSaleService.getAuctionRequest();
        return ResponseEntity.ok(auctionRequests);
    }

    @PutMapping("/cancel-request-auction/{status}/{auctionrequestid}")
    public ResponseEntity<AuctionRequest> cancelAuctionRequest(@PathVariable("status") String status,
                                                               @PathVariable("auctionrequestid") long auctionrequestid){
        AuctionRequest auctionRequest = auctionSaleService.cancelRequest(status, auctionrequestid);
        return ResponseEntity.ok(auctionRequest);
    }

    @GetMapping("/list-auction-request-by-status/{status}")
    public ResponseEntity<List<AuctionRequest>> getAuctionRequestByStatus(@PathVariable("status") String status) {
        List<AuctionRequest> auctionRequests = auctionSaleService.getAuctionRequestByStatus(status);
        return ResponseEntity.ok(auctionRequests);
    }
}
