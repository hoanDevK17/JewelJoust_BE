package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.AuctionRegistration;
import online.jeweljoust.BE.entity.AuctionSession;
import online.jeweljoust.BE.enums.AuctionSessionStatus;
import online.jeweljoust.BE.model.AuctionSessionDetailResponse;
import online.jeweljoust.BE.model.AuctionSessionRequest;
import online.jeweljoust.BE.model.PagedResponse;
import online.jeweljoust.BE.respository.AuctionRegistrationRepository;

import online.jeweljoust.BE.service.AuctionSessionService;
import online.jeweljoust.BE.utils.AccountUtils;
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
public class AuctionSessionAPI {

    @Autowired
    AuctionSessionService auctionSessionService;


    @PostMapping("/auctionSessions")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<AuctionSession> createAuctionsSession(@RequestBody AuctionSessionRequest auctionSessionRequest) {

        AuctionSession auctionSession = auctionSessionService.addAuctionSessions(auctionSessionRequest);

        return ResponseEntity.ok(auctionSession);
    }
//    getAll
//    @GetMapping("/auctionSessions")
//
//    public ResponseEntity<List<AuctionSession>> getAllAuctionSessions() {
//        List<AuctionSession> auctionSession = auctionSessionService.getAllAuctionSessions();
//        return ResponseEntity.ok(auctionSession);
//    }
    @GetMapping("/auctionSessions/{status}")
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<List<AuctionSession>> getAuctionSessionsbyStatus(@PathVariable AuctionSessionStatus status) {

        List<AuctionSession> auctionSession = auctionSessionService.getAuctionSessionsByStatus(status);
        return ResponseEntity.ok(auctionSession);
    }
    @GetMapping("/auctionSessions/registered")
    public ResponseEntity<List<AuctionSession>> getAuctionRegistered() {

        List<AuctionSession> auctionSession = auctionSessionService.getAuctionRegistered();
        return ResponseEntity.ok(auctionSession);
    }
    @GetMapping("/auctionSessions/detail/{id}")
    public ResponseEntity<AuctionSessionDetailResponse> getAuctionSessionByID(@PathVariable long id, @RequestParam(required = false)  long userId) {
        AuctionSessionDetailResponse auctionSession = auctionSessionService.getAuctionSessionByID(id,userId);
        return ResponseEntity.ok(auctionSession);
    }
//    update
    @PutMapping("/auctionSessions/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'STAFF')")

    public ResponseEntity<AuctionSession> updateAuctionSessions(@PathVariable Long id,@RequestBody AuctionSessionRequest auctionSessionRequest) {
        AuctionSession auctionSession =  auctionSessionService.updateAuctionSession(id, auctionSessionRequest);
        return ResponseEntity.ok(auctionSession);
    }
    @GetMapping("/auctionSessions/name/{name}")

    public ResponseEntity<Page<AuctionSession>>findAuctionSessionByName(Pageable pageable, @PathVariable(required = false) String name) {
        if (name == null) {
            return ResponseEntity.ok(auctionSessionService.getAllAuctionSessionsPaging(pageable));
        } else {
            return ResponseEntity.ok(auctionSessionService.findSessionByName(pageable, name ));
        }

    }

    @PutMapping("/auctionSessions/stop")

    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<AuctionSession> stopAuctionSessions(@RequestBody Long id) {

        AuctionSession auctionSession =  auctionSessionService.stopAuctionSession(id);
        return ResponseEntity.ok(auctionSession);
    }
    @PutMapping("/auctionSessions/continue")
    public ResponseEntity<AuctionSession> continueAuctionSessions(@RequestBody Long id) {
        AuctionSession auctionSession =  auctionSessionService.continueAuctionSession(id);
        return ResponseEntity.ok(auctionSession);
    }

    @GetMapping("/auctionSessions/3days")
    public ResponseEntity<List<AuctionSession>> getAuctionSession3days() {
        List<AuctionSession> auctionSession =  auctionSessionService.getAuctionSession3days();
        return ResponseEntity.ok(auctionSession);
    }

//    @GetMapping("/auctionSessions/paging")
//    public PagedResponse getSession (@RequestParam int page,
//                                     @RequestParam int size) {
//        return auctionSessionService.getSession(page, size);
//    }

    @GetMapping("/auctionSessions/paging")
    public ResponseEntity<Page<AuctionSession>>getSessionPaging(Pageable pageable) {
        return ResponseEntity.ok(auctionSessionService.getAllAuctionSessions(pageable));
    }

    @PutMapping("/auctionSessions/finish")
    public ResponseEntity<AuctionSession>auctionSessionFinish(@RequestBody Long id) {
        return ResponseEntity.ok(auctionSessionService.auctionSessionFinish(id));
    }
//    @DeleteMapping("/auctionSessions/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        auctionSessionService.deleteSession(id);
//        return ResponseEntity.noContent().build(); // Trả về HTTP 204 No Content
//    }
//  mở phiên khi đến giờ ( staff)
//   kết thúc phiên khi đến giờ (staff)
    // dừng phiên khẩn cấp để tí tiếp tucj lại

//    @GetMapping("/abc/{id}")
//    public ResponseEntity<List<AuctionRegistration>> abc(@PathVariable long id) {
//        return ResponseEntity.ok(auctionRegistrationRepository.findAuctionRegistrationByAuctionSessionId(id));
//    }


}
