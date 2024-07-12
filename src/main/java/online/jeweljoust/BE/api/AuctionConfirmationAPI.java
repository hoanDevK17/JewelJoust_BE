package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.model.MemberConfirmRequest;
import online.jeweljoust.BE.service.AuctionConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")

public class AuctionConfirmationAPI {

    @Autowired
    AuctionConfirmationService confirmationService;

    @PutMapping("/auctionConfirmation/confirmed")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<AuctionRequest> confirmedAuctionByMember(@RequestBody MemberConfirmRequest memberConfirmRequest) {
        AuctionRequest auctionRequest = confirmationService.confirmByMember(memberConfirmRequest);
        return ResponseEntity.ok(auctionRequest)        ;
    }

    @PutMapping("/auctionConfirmation/rejected")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<AuctionRequest> rejectedAuctionByMember(@RequestBody MemberConfirmRequest memberConfirmRequest) {
        AuctionRequest auctionRequest = confirmationService.rejectByMember(memberConfirmRequest);
        return ResponseEntity.ok(auctionRequest);
    }
}
