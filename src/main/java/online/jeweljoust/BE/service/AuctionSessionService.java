package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.entity.AuctionSession;

import online.jeweljoust.BE.entity.Shipment;
import online.jeweljoust.BE.enums.AccountRole;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import online.jeweljoust.BE.enums.AuctionSessionStatus;
import online.jeweljoust.BE.enums.ShipmentStatus;
import online.jeweljoust.BE.model.AuctionSessionRequest;
import online.jeweljoust.BE.respository.AuctionRequestRepository;
import online.jeweljoust.BE.respository.AuctionSessionRepository;
import online.jeweljoust.BE.respository.AuthenticationRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class AuctionSessionService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    @Autowired
    AuctionSessionRepository auctionSessionRepository;
    @Autowired
    AccountUtils accountUtils;
    @Autowired
    AuthenticationRepository authenticationRepository;
    @Autowired
    AuctionRequestRepository auctionRepository;

    private ExecutorService executorService = Executors.newFixedThreadPool(10); // Sử dụng 10 luồng

    public List<AuctionSession> getAllAuctionSessions() {
        return auctionSessionRepository.findAll();
    }
    public List<AuctionSession> getAuctionSessionsByStatus(AuctionSessionStatus status) {
        return auctionSessionRepository.findByStatus(status);
    }
    public AuctionSession getAuctionSessionByID(long id) {
        return auctionSessionRepository.findAuctionSessionById(id);
    }
    public AuctionSession addAuctionSessions(AuctionSessionRequest auctionSessionRequest) {

        AuctionSession auctionSession = new AuctionSession();
        AuctionRequest auctionRequest = auctionRepository
                .findAuctionRequestById(auctionSessionRequest.getAuction_request_id());

        if (!auctionRequest.getStatus().equals(AuctionRequestStatus.AGREED)) {
            throw new IllegalStateException("Not support this AuctionRequest");
        }
        else if(auctionRequest.getAuctionSessions() != null){
            throw new IllegalStateException("Session has initial");
        }
        auctionSession.setAuctionRequest(auctionRequest);

        auctionSession.setStaffSession(authenticationRepository.findById(auctionSessionRequest.getStaff_id()));
        auctionSession.setStart_time(auctionSessionRequest.getStart_time());
        auctionSession.setEnd_time(auctionSessionRequest.getEnd_time());
        auctionSession.setInitialPrice(auctionRequest.getUltimateValuation().getPrice());
        auctionSession.setMinStepPrice(auctionSessionRequest.getMin_stepPrice());
        auctionSession.setDepositAmount(auctionSessionRequest.getDeposit_amount());
        auctionSession.setNameSession(auctionSessionRequest.getName_session());
        auctionSession.setNameJewelry(auctionSessionRequest.getName_jewelry());
        auctionSession.setDescription(auctionSessionRequest.getDescription());
        auctionSession.setFeeAmount(0.05);
        auctionSession.setCreateAt(new Date());
        auctionSession.setStatus(AuctionSessionStatus.INITIALIZED);
        // Initialized: khởi tạo thành công , chưa tới giờ đấu giá
        // Bidding: đang được đấu giá
        // Pending Payment: chờ thanh toán
        // Completed : bán hoàn tất
        // Cancelled : đã hủy

        return auctionSessionRepository.save(auctionSession);
    }

    public AuctionSession updateAuctionSession(long id, AuctionSessionRequest auctionSessionRequest) {
        AuctionSession auctionSession = auctionSessionRepository.findAuctionSessionById(id);
        auctionSession.setAuctionRequest(
                auctionRepository.findAuctionRequestById(auctionSessionRequest.getAuction_request_id()));
//        auctionSession.setManagerSession(accountUtils.getAccountCurrent());
        auctionSession.setStaffSession(authenticationRepository.findById(auctionSessionRequest.getStaff_id()));
        auctionSession.setStart_time(auctionSessionRequest.getStart_time());
        auctionSession.setEnd_time(auctionSessionRequest.getEnd_time());
        auctionSession.setInitialPrice(auctionSession.getAuctionRequest().getUltimateValuation().getPrice());
        auctionSession.setMinStepPrice(auctionSessionRequest.getMin_stepPrice());
        auctionSession.setDepositAmount(auctionSessionRequest.getDeposit_amount());
        auctionSession.setNameSession(auctionSessionRequest.getName_session());
        auctionSession.setNameJewelry(auctionSessionRequest.getName_jewelry());
        auctionSession.setDescription(auctionSessionRequest.getDescription());
        // auctionSession.setFeeAmount(auctionSessionRequest.getFee_amount());
        // auctionSession.setCreateAt(new Date());
        auctionSession.setStatus(AuctionSessionStatus.INITIALIZED);
        return auctionSessionRepository.save(auctionSession);
    }

    @Scheduled(cron = "0 * * * * ?")
    public void checkTimeSessionStart(){
        List<AuctionSession> startSesion = auctionSessionRepository.findByStatus(AuctionSessionStatus.INITIALIZED);
        List<AuctionSession> endSesion = auctionSessionRepository.findByStatus(AuctionSessionStatus.BIDDING);
        Date now = new Date();
        for (AuctionSession s: startSesion){
            if (now.equals(s.getStart_time()) || now.after(s.getStart_time())){
                executorService.submit(() -> {
                    s.setStatus(AuctionSessionStatus.BIDDING);
                    auctionSessionRepository.save(s);
                });
            }
        }
        for (AuctionSession s: endSesion){
            if (now.equals(s.getEnd_time()) || now.after(s.getEnd_time())){
                executorService.submit(() -> {
                    s.setStatus(AuctionSessionStatus.PENDINGPAYMENT);
                    auctionSessionRepository.save(s);
                });
            }
        }
    }

    public AuctionSession stopAuctionSession(Long id) {
        AuctionSession auctionSession = auctionSessionRepository.findAuctionSessionById(id);
        if (auctionSession.getStatus().equals(AuctionSessionStatus.BIDDING)){
            auctionSession.setStatus(AuctionSessionStatus.STOP);
            auctionSessionRepository.save(auctionSession);
        }
        return auctionSession;
    }
}
