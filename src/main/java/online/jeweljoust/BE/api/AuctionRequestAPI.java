package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.AuctionRequest;

import online.jeweljoust.BE.enums.AuctionRequestStatus;
import online.jeweljoust.BE.model.AuctionRequestReponse;
import online.jeweljoust.BE.model.PagedResponse;
import online.jeweljoust.BE.service.AuctionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PostMapping("/auctionRequests")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity requestSaleAuction(@RequestBody AuctionRequestReponse auctionRequestReponse) {
        AuctionRequest auctionRequest = auctionRequestService.requestSale(auctionRequestReponse);
        return ResponseEntity.ok(auctionRequest);
    }

    @GetMapping("/auctionRequests/accountCurrent")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<List<AuctionRequest>> getAuctionByUserid() {
        List<AuctionRequest> auctionRequests = auctionRequestService.getAuctionRequest();
        return ResponseEntity.ok(auctionRequests);
    }

    @PutMapping("/auctionRequests/{id}/cancel")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<AuctionRequest> cancelAuctionRequest(@PathVariable("id") long id){
        AuctionRequest auctionRequest = auctionRequestService.cancelRequest(id);
        return ResponseEntity.ok(auctionRequest);
    }

    @GetMapping("/auctionRequests/{status}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<List<AuctionRequest>> getAuctionRequestByStatus(@PathVariable("status") AuctionRequestStatus status) {
        List<AuctionRequest> auctionRequests = auctionRequestService.getAuctionRequestByStatus(status);
        return ResponseEntity.ok(auctionRequests);
    }

//    @GetMapping("/auctionRequests")
////    @PreAuthorize("hasAuthority('ADMIN', 'MANAGER', 'STAFF')")
//    public ResponseEntity<List<AuctionRequest>> getAllAuctionRequest() {
//        List<AuctionRequest> auctionRequests = auctionRequestService.getAllAuctionRequest();
//        return ResponseEntity.ok(auctionRequests);
//    }

    @GetMapping("/auctionRequests/available")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<List<AuctionRequest>> getAllAuctionRequestAvailable() {
        List<AuctionRequest> auctionRequests = auctionRequestService.getAllAuctionRequestAvailable();
        return ResponseEntity.ok(auctionRequests);
    }

    @GetMapping("/auctionRequests/paging")
    public ResponseEntity<Page<AuctionRequest>> getRequestPaging(Pageable pageable) {
        return ResponseEntity.ok(auctionRequestService.getRequestPaging(pageable));
    }
//    @GetMapping("/auctionRequest/accountCurrent")
////    @PreAuthorize("hasAuthority('MEMBER')")
//    public ResponseEntity<List<AuctionRequest>> getAllAuctionRequestById() {
//        List<AuctionRequest> auctionRequests = auctionRequestService.getAllAuctionRequestById();
//        return ResponseEntity.ok(auctionRequests);
//    }
}
