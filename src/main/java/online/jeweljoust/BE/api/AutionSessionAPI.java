package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.Account;


import online.jeweljoust.BE.entity.AutionSession;
import online.jeweljoust.BE.model.AutionSessionRequest;
import online.jeweljoust.BE.service.AuthenticationService;
import online.jeweljoust.BE.service.AutionSessionService;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")
public class AutionSessionAPI {

    @Autowired
    AutionSessionService autionSessionService;
    @Autowired
    AccountUtils accountUtils;
    @PostMapping("/createAutionSession")
    public ResponseEntity<AutionSession> createAutionsSession(@RequestBody AutionSessionRequest autionSessionRequest) {

        AutionSession autionSession = new AutionSession();
        autionSession.setManager_id(accountUtils.getAccountCurrent().getUserid());
        autionSession.setStaff_id(autionSessionRequest.getStaff_id());
        autionSession.setStart_time(autionSessionRequest.getStart_time());
        autionSession.setEnd_time(autionSessionRequest.getEnd_time());
        autionSession.setInitial_price(autionSessionRequest.getInitial_price());
        autionSession.setMin_stepPrice(autionSessionRequest.getMin_stepPrice());
        autionSession.setDeposit_amount(autionSessionRequest.getDeposit_amount());
        autionSession.setName_session(autionSessionRequest.getName_session());
        autionSession.setName_jewelrys(autionSessionRequest.getName_jewelrys());
        autionSession.setDescription(autionSessionRequest.getDescription());
        autionSession.setFee_amount(autionSessionRequest.getFee_amount());
        autionSession.setCreate_at(new Date());
        autionSession.setStatus("Initialized");
//        Initialized: khởi tạo thành công , chưa tới giờ đấu giá
//        Bidding: đang được đấu giá
//        Pending Payment: chờ thanh toán
//        Completed : bán hoàn tất
//        Cancelled : đã hủy





        AutionSession autionSessions = autionSessionService.addAutionSessions(autionSession);

        return ResponseEntity.ok(autionSessions);
    }
    @GetMapping("/getAllAutionSessions")
    public ResponseEntity<List<AutionSession>> getAutionSessions() {
        List<AutionSession> autionSession = autionSessionService.getAllAutionSessions();
        return ResponseEntity.ok(autionSession);
    }

}
