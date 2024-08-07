package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.AuctionRegistration;
import online.jeweljoust.BE.model.AuctionRegistrationRequest;
import online.jeweljoust.BE.respository.AuctionRegistrationRepository;
import online.jeweljoust.BE.service.AuctionRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")

public class AuctionRegistrationAPI {
//    đăng ký tham gia đấu giá
    @Autowired
    AuctionRegistrationRepository auctionRegistrationRepository;
    @Autowired
    AuctionRegistrationService  auctionRegistrationService;

    @PostMapping("/auctionRegistrations")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<AuctionRegistration>  createAuctionRegistrations(@RequestBody AuctionRegistrationRequest auctionRegistrationRequest) {
    AuctionRegistration auctionRegistration = auctionRegistrationService.addAuctionRegistration(auctionRegistrationRequest);
        return ResponseEntity.ok(auctionRegistration);
    }
    @GetMapping("/auctionRegistrations")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'STAFF','MEMBER')")
    public ResponseEntity<List<AuctionRegistration>>  getAllAuctionRegistrations() {
        List<AuctionRegistration> auctionRegistrations = auctionRegistrationService.findAllAuctionRegistration();
        return ResponseEntity.ok(auctionRegistrations);
    }
    @PutMapping("/auctionRegistrations/{id}/cancel")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity cancelAuctionRegistrations(@PathVariable Long id) {
        AuctionRegistration auctionRegistration = auctionRegistrationService.cancelAuctionRegistration(id);
        return ResponseEntity.ok(auctionRegistration);
    }
//    @PutMapping("/auctionRegistrations/{id}/deposit")
//    public ResponseEntity depositAuctionRegistrations(@PathVariable Long id) {
//        AuctionRegistration auctionRegistration = auctionRegistrationService.depositAuctionRegistration(id);
//        return ResponseEntity.ok(auctionRegistration);
//    }

}
