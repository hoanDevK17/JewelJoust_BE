package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.AuctionBid;
import online.jeweljoust.BE.model.AuctionBidRequest;
import online.jeweljoust.BE.service.AuctionBidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")

public class AuctionBidAPI {
    @Autowired
    AuctionBidService auctionBidService;
    @PostMapping("/auctionBids")
    @PreAuthorize("hasAuthority('MEMBER')")

    public ResponseEntity<AuctionBid> addBid(@RequestBody AuctionBidRequest auctionBidRequest) {
        AuctionBid auctionBid = auctionBidService.addAuctionBid(auctionBidRequest);
        return ResponseEntity.ok(auctionBid);
    }

    @GetMapping("/auctionBids")
    public ResponseEntity getAllBid() {
        List<AuctionBid> auctionBids  = auctionBidService.getHistoryAuctionBid();
        return ResponseEntity.ok(auctionBids);
    }
    @GetMapping("/auctionBids/session/{sessionId}")
    public ResponseEntity getlistCurrentBids(@PathVariable long sessionId) {
//        get 5 Element top
        List<AuctionBid> auctionBids  = auctionBidService.getListCurrentBidsBySessionId(sessionId);
        return ResponseEntity.ok(auctionBids);
    }
    @GetMapping("/auctionBids/session/getAll/{sessionId}")
    public ResponseEntity getAllAuctionBidById(@PathVariable long sessionId) {
        List<AuctionBid> auctionBids  = auctionBidService.getListCurrentBidsBySessionId(sessionId);
        return ResponseEntity.ok(auctionBids);
    }

//    @GetMapping("/auctionBids/{registrationid}")
//    public ResponseEntity<List<AuctionBid>> getBidHistory(@PathVariable("registrationId") long registrationId) {
//        List<AuctionBid> bids = auctionBidService.getBidHistoryByAccountRegistrationId(registrationId);
//        return ResponseEntity.ok(bids);
//    }
//    @GetMapping
//    //    @PreAuthorize("hasAuthority('MEMBER')")
//    public ResponseEntity getAllBid(@RequestBody AuctionBidRequest auctionBidRequest) {
//        AuctionBid auctionBid = auctionBidService.addAuctionBid(auctionBidRequest);
//        return ResponseEntity.ok(auctionBid);
//    }

}
