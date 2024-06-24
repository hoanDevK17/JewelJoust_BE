package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;


import online.jeweljoust.BE.entity.AuctionSession;
import online.jeweljoust.BE.model.AuctionSessionRequest;
import online.jeweljoust.BE.respository.AuctionSessionRepository;
import online.jeweljoust.BE.respository.AuthenticationRepository;
import online.jeweljoust.BE.service.AuctionSessionService;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")
public class AuctionSessionAPI {
@Autowired
    AuthenticationRepository authenticationRepository;
    AuctionSessionRepository auctionSessionRepository;
    @Autowired
    AuctionSessionService auctionSessionService;
    @Autowired
    AccountUtils accountUtils;
    @PostMapping("/auctionSessions")
    public ResponseEntity<AuctionSession> createAuctionsSession(@RequestBody AuctionSessionRequest auctionSessionRequest) {

        AuctionSession auctionSession = auctionSessionService.addAuctionSessions(auctionSessionRequest);

        return ResponseEntity.ok(auctionSession);
    }
//    getAll
    @GetMapping("/auctionSessions")
    public ResponseEntity<List<AuctionSession>> getAllAuctionSessions() {
        List<AuctionSession> auctionSession = auctionSessionService.getAllAuctionSessions();
        return ResponseEntity.ok(auctionSession);
    }
    @GetMapping("/auctionSessions/id/{id}")
    public ResponseEntity<AuctionSession> getAuctionSessionByID(@PathVariable long id) {
        AuctionSession auctionSession = auctionSessionService.getAuctionSessionByID(id);
        return ResponseEntity.ok(auctionSession);
    }
//    update
    @PutMapping("/auctionSessions/{id}")
    public ResponseEntity<AuctionSession> updateAuctionSessions(@PathVariable Long id,@RequestBody AuctionSessionRequest auctionSessionRequest) {
        AuctionSession auctionSession =  auctionSessionService.updateAuctionSession(id, auctionSessionRequest);
        return ResponseEntity.ok(auctionSession);
    }
    @GetMapping("/auctionSessions/name/{name}")
    public ResponseEntity<List<AuctionSession>>findAuctionSessionByName(@PathVariable String name) {
        return ResponseEntity.ok(auctionSessionRepository.findByNameSession(name));
    }
//  mở phiên khi đến giờ ( staff)
//   kết thúc phiên khi đến giờ (staff)
    // dừng phiên khẩn cấp để tí tiếp tucj lại

}
