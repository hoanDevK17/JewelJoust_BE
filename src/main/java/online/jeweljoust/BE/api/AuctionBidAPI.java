package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.AuctionBid;
import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.model.AuctionBidRequest;
import online.jeweljoust.BE.model.MemberConfirmRequest;
import online.jeweljoust.BE.respository.AuctionBidRepository;
import online.jeweljoust.BE.service.AuctionBidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")

public class AuctionBidAPI {
    @Autowired
    AuctionBidService auctionBidService;

    @PostMapping("/auctionBid")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<AuctionBid> addBid(@RequestBody AuctionBidRequest auctionBidRequest) {
        AuctionBid auctionBid = auctionBidService.addAuctionBid(auctionBidRequest);
        return ResponseEntity.ok(auctionBid);
    }
//    @GetMapping
//    //    @PreAuthorize("hasAuthority('MEMBER')")
//    public ResponseEntity getAllBid(@RequestBody AuctionBidRequest auctionBidRequest) {
//        AuctionBid auctionBid = auctionBidService.addAuctionBid(auctionBidRequest);
//        return ResponseEntity.ok(auctionBid);
//    }

}
