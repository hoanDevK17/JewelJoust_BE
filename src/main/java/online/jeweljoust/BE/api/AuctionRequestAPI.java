package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.AuctionRequest;

import online.jeweljoust.BE.model.AuctionRequestReponse;
import online.jeweljoust.BE.service.AuctionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")

public class AuctionRequestAPI {

    @Autowired
    AuctionRequestService auctionRequestService;

    @PostMapping("/request-sale")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity requestSaleAuction(@RequestBody AuctionRequestReponse auctionRequestReponse) {
        AuctionRequest auctionRequest = auctionRequestService.requestSale(auctionRequestReponse);
        return ResponseEntity.ok(auctionRequest);
    }


//    @GetMapping("/auction-request-by-userid")
//    public ResponseEntity<List<AuctionRequest>> getAuctionByUserid() {
//        List<AuctionRequest> auctionRequests = auctionRequestService.getAuctionRequest();
//        return ResponseEntity.ok(auctionRequests);
//    }

//    @PutMapping("/cancel-request-auction/{status}/{auctionrequestid}")
//    @PreAuthorize("hasAuthority('MEMBER')")
//    public ResponseEntity<AuctionRequest> cancelAuctionRequest(@PathVariable("auctionrequestid") long auctionrequestid){
////        AuctionRequest auctionRequest = auctionRequestService.cancelRequest(auctionrequestid);
//        return ResponseEntity.ok(auctionRequest);
//    }

    @GetMapping("/list-auction-request-by-status/{status}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<List<AuctionRequest>> getAuctionRequestByStatus(@PathVariable("status") String status) {
        List<AuctionRequest> auctionRequests = auctionRequestService.getAuctionRequestByStatus(status);
        return ResponseEntity.ok(auctionRequests);
    }
}
