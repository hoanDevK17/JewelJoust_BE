package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.*;

import online.jeweljoust.BE.enums.*;
import online.jeweljoust.BE.mapper.AuctionSessionMapper;
import online.jeweljoust.BE.model.*;
import online.jeweljoust.BE.respository.*;
import online.jeweljoust.BE.utils.AccountUtils;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    AuctionRegistrationRepository auctionRegistrationRepository;
    @Autowired
    AuctionSessionMapper auctionSessionMapper;
    @Autowired
    EmailService emailService;
    private ExecutorService executorService = Executors.newFixedThreadPool(10); // Sử dụng 10 luồng

    public List<AuctionSession> getAllAuctionSessions() {
        return auctionSessionRepository.findAll();
    }
    public List<AuctionSession> getAuctionSessionsByStatus(AuctionSessionStatus status) {
        return auctionSessionRepository.findByStatus(status);
    }
    public AuctionSessionDetailResponse getAuctionSessionByID(long id,long idUser) {
        AuctionSession auctionSession = auctionSessionRepository.findAuctionSessionById(id);
        if(auctionSession == null) {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found: " + id);
        }
        boolean isRegistered;
        if(idUser >0 ){
            isRegistered = auctionRegistrationRepository.existsByAccountIdAndSessionId(idUser, id);
        }
        else{ isRegistered = false;}
        AuctionSessionReponse auctionSessionReponse = new AuctionSessionReponse();

        AuctionSessionDetailResponse auctionSessionDetailResponse = auctionSessionMapper.toResponse(auctionSession);
        auctionSessionDetailResponse.setRegister(isRegistered);
        return auctionSessionDetailResponse;
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
        auctionSession.setMinStepPrice(auctionSessionRequest.getMin_stepPrice());
        auctionSession.setDepositAmount(auctionSessionRequest.getDeposit_amount());
        auctionSession.setNameSession(auctionSessionRequest.getName_session());
        auctionSession.setNameJewelry(auctionSessionRequest.getName_jewelry());
        auctionSession.setDescription(auctionSessionRequest.getDescription());
        auctionSession.setFeeAmount(0.05);
        auctionSession.setCreateAt(new Date());
        auctionSession.setStatus(AuctionSessionStatus.INITIALIZED);

        for (ResourceRequest resourceRequest : auctionSessionRequest.getResourceSession()){
            Resources resources = new Resources();
            resources.setResourceType(ResourceTypes.ResourceType.img);
            resources.setPath(resourceRequest.getPath());
            resources.setDescription(resourceRequest.getDescription());
            resources.setReferenceType(ResourceTypes.ReferenceType.AUCTION_SESSION);
            resources.setAuctionSessionResource(auctionSession);
//            resources.setAccountResource(accountUtils.getAccountCurrent());
            resources.setUploadAt(new Date());
            resourceRepository.save(resources);
        }
        // Initialized: khởi tạo thành công , chưa tới giờ đấu giá
        // AuctionBid: đang được đấu giá
        // Pending Payment: chờ thanh toán
        // Completed : bán hoàn tất
        // Cancelled : đã hủy

        return auctionSessionRepository.save(auctionSession);
    }

    public AuctionSession updateAuctionSession(long id, AuctionSessionRequest auctionSessionRequest) {
        AuctionSession auctionSession = auctionSessionRepository.findAuctionSessionById(id);
//        auctionSession.setManagerSession(accountUtils.getAccountCurrent());
        if (auctionSession.getStatus().equals(AuctionSessionStatus.INITIALIZED) || auctionSession.getStatus().equals(AuctionSessionStatus.STOP) || auctionSession.getStatus().equals(AuctionSessionStatus.BIDDING)){
            auctionSession.setStaffSession(authenticationRepository.findById(auctionSessionRequest.getStaff_id()));
            auctionSession.setStart_time(auctionSessionRequest.getStart_time());
            auctionSession.setEnd_time(auctionSessionRequest.getEnd_time());
            auctionSession.setMinStepPrice(auctionSessionRequest.getMin_stepPrice());
            auctionSession.setDepositAmount(auctionSessionRequest.getDeposit_amount());
            auctionSession.setNameSession(auctionSessionRequest.getName_session());
            auctionSession.setNameJewelry(auctionSessionRequest.getName_jewelry());
            auctionSession.setDescription(auctionSessionRequest.getDescription());
            // auctionSession.setFeeAmount(auctionSessionRequest.getFee_amount());
            // auctionSession.setCreateAt(new Date());
            Date now = new Date();
            Date start = auctionSession.getStart_time();
            Date end = auctionSession.getEnd_time();

            if (now.after(start) && (now.before(end))){
                auctionSession.setStatus(AuctionSessionStatus.BIDDING);
            } else if (now.after(end)){
                auctionSession.setStatus(AuctionSessionStatus.PENDINGPAYMENT);
            } else if (now.before(start)){
                auctionSession.setStatus(AuctionSessionStatus.INITIALIZED);
            }

            List<AuctionRegistration> allRegister = auctionRegistrationRepository.findAuctionRegistrationByAuctionSessionId(id);
            for (AuctionRegistration registration : allRegister){
                Account account = registration.getAccountRegistration();
                EmailDetail emailDetail = new EmailDetail();
                emailDetail.setRecipient(account.getEmail());
                emailDetail.setSubject("Preliminary Appraisal Complete");
                emailDetail.setProductName(registration.getAuctionSession().getNameJewelry());
                emailDetail.setFullName(account.getFullname());
                emailService.sendMailNotificationSession(emailDetail, "templateUpdateSession");
            }
            return auctionSessionRepository.save(auctionSession);
        } else {
            throw new IllegalStateException("The auction has been completed, you can no longer modify it");
        }
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
    public void deleteSession(Long id) {
         auctionSessionRepository.deleteById(id);
    }
}
