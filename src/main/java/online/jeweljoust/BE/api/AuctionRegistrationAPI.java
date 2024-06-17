package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.AuctionRegistration;
import online.jeweljoust.BE.model.AuctionRegistrationRequest;
import online.jeweljoust.BE.respository.AuctionRegistrationRepository;
import online.jeweljoust.BE.service.AuctionRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<AuctionRegistration>  createAuctionRegistrations(@RequestBody AuctionRegistrationRequest auctionRegistrationRequest) {
    AuctionRegistration auctionRegistration = auctionRegistrationService.addAuctionRegistration(auctionRegistrationRequest);
        return ResponseEntity.ok(auctionRegistration);
    }
    @GetMapping("/auctionRegistrations")
    public ResponseEntity<List<AuctionRegistration>>  getAllAuctionRegistrations() {
        List<AuctionRegistration> auctionRegistrations = auctionRegistrationService.findAllAuctionRegistration();
        return ResponseEntity.ok(auctionRegistrations);
    }
    @PutMapping("/auctionRegistrations/{id}/cancel")
    public ResponseEntity cancelAuctionRegistrations(@PathVariable Long id) {
        AuctionRegistration auctionRegistration = auctionRegistrationService.cancelAuctionRegistration(id);
        return ResponseEntity.ok(auctionRegistration);
    }
    @PutMapping("/auctionRegistrations/{id}/deposit")
    public ResponseEntity depositAuctionRegistrations(@PathVariable Long id) {
        AuctionRegistration auctionRegistration = auctionRegistrationService.depositAuctionRegistration(id);
        return ResponseEntity.ok(auctionRegistration);
    }

}
