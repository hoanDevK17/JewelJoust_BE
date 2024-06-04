package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;


import online.jeweljoust.BE.entity.AuctionSession;
import online.jeweljoust.BE.model.AuctionSessionRequest;
import online.jeweljoust.BE.service.AuctionSessionService;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")
public class AuctionSessionAPI {

    @Autowired
    AuctionSessionService autionSessionService;
    @Autowired
    AccountUtils accountUtils;
    @PostMapping("/createAuctionSession")
    public ResponseEntity<AuctionSession> createAuctionsSession(@RequestBody AuctionSessionRequest auctionSessionRequest) {

        AuctionSession auctionSession = new AuctionSession();
        auctionSession.setManager_id(accountUtils.getAccountCurrent().getId());
        auctionSession.setStaff_id(auctionSessionRequest.getStaff_id());
        auctionSession.setStart_time(auctionSessionRequest.getStart_time());
        auctionSession.setEnd_time(auctionSessionRequest.getEnd_time());
        auctionSession.setInitial_price(auctionSessionRequest.getInitial_price());
        auctionSession.setMin_stepPrice(auctionSessionRequest.getMin_stepPrice());
        auctionSession.setDeposit_amount(auctionSessionRequest.getDeposit_amount());
        auctionSession.setName_session(auctionSessionRequest.getName_session());
        auctionSession.setName_jewelrys(auctionSessionRequest.getName_jewelrys());
        auctionSession.setDescription(auctionSessionRequest.getDescription());
        auctionSession.setFee_amount(auctionSessionRequest.getFee_amount());
        auctionSession.setCreate_at(new Date());
        auctionSession.setStatus("Initialized");
//        Initialized: khởi tạo thành công , chưa tới giờ đấu giá
//        Bidding: đang được đấu giá
//        Pending Payment: chờ thanh toán
//        Completed : bán hoàn tất
//        Cancelled : đã hủy





        AuctionSession auctionSessions = autionSessionService.addAutionSessions(auctionSession);

        return ResponseEntity.ok(auctionSessions);
    }
    @GetMapping("/getAllAuctionSessions")
    public ResponseEntity<List<AuctionSession>> getAutionSessions() {
        List<AuctionSession> auctionSession = autionSessionService.getAllAutionSessions();
        return ResponseEntity.ok(auctionSession);
    }

}
