package online.jeweljoust.BE.service;

import jakarta.transaction.Transactional;
import online.jeweljoust.BE.entity.*;

import online.jeweljoust.BE.enums.*;
import online.jeweljoust.BE.mapper.AuctionSessionMapper;
import online.jeweljoust.BE.model.*;
import online.jeweljoust.BE.respository.*;
import online.jeweljoust.BE.utils.AccountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    AuctionBidRepository auctionBidRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    WalletService walletService;
    private ExecutorService executorService = Executors.newFixedThreadPool(10); // Sử dụng 10 luồng

    public List<AuctionSession> getAllAuctionSessions() {
        return auctionSessionRepository.findAll();
    }

    public List<AuctionSession> getAuctionSessionsByStatus(AuctionSessionStatus status) {
        return auctionSessionRepository.findAuctionSessionByStatus(status);
    }
    public List<AuctionSession> getAuctionRegistered() {

        return auctionSessionRepository.findAuctionSessionRegisteredByUserId(accountUtils.getAccountCurrent().getId());
    }

    public AuctionSessionDetailResponse getAuctionSessionByID(long id,long idUser) {

        AuctionSession auctionSession = auctionSessionRepository.findAuctionSessionById(id);
        if (auctionSession == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found: " + id);
        }
        boolean isRegistered;
        if (idUser > 0) {
            isRegistered = auctionRegistrationRepository.existsByAccountIdAndSessionId(idUser, id);
        } else {
            isRegistered = false;
        }
        AuctionSessionReponse auctionSessionReponse = new AuctionSessionReponse();

        AuctionSessionDetailResponse auctionSessionDetailResponse = auctionSessionMapper.toResponse(auctionSession);
        auctionSessionDetailResponse.setRegister(isRegistered);
        AuctionBid highestBid = auctionBidRepository.findHighestBidBySessionId(auctionSession.getId())
                .orElse(new AuctionBid());
        if (highestBid.getBid_price() != null) {
            auctionSessionDetailResponse.setHighestPrice(highestBid.getBid_price());
        }
        return auctionSessionDetailResponse;
    }

    public AuctionSession addAuctionSessions(AuctionSessionRequest auctionSessionRequest) {

        AuctionSession auctionSession = new AuctionSession();
        AuctionRequest auctionRequest = auctionRepository
                .findAuctionRequestById(auctionSessionRequest.getAuction_request_id());

        if (!auctionRequest.getStatus().equals(AuctionRequestStatus.AGREED)) {
            throw new IllegalStateException("Not support this AuctionRequest");
        } else if (auctionRequest.getAuctionSessions() != null) {
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
        auctionSessionRepository.save(auctionSession);
        for (ResourceRequest resourceRequest : auctionSessionRequest.getResourceSession()) {
            Resources resources = new Resources();
            resources.setResourceType(ResourceTypes.ResourceType.img);
            resources.setPath(resourceRequest.getPath());
            resources.setDescription(resourceRequest.getDescription());
            resources.setReferenceType(ResourceTypes.ReferenceType.AUCTION_SESSION);
            resources.setAuctionSessionResource(auctionSession);
            resources.setAccountResource(accountUtils.getAccountCurrent());
            resources.setUploadAt(new Date());
            resourceRepository.save(resources);
        }
        // Initialized: khởi tạo thành công , chưa tới giờ đấu giá
        // AuctionBid: đang được đấu giá
        // Pending Payment: chờ thanh toán
        // Completed : bán hoàn tất
        // Cancelled : đã hủy

        return auctionSession;
    }

    @Transactional
    public AuctionSession updateAuctionSession(long id, AuctionSessionRequest auctionSessionRequest) {
        AuctionSession auctionSession = auctionSessionRepository.findAuctionSessionById(id);
        // auctionSession.setManagerSession(accountUtils.getAccountCurrent());
        if (auctionSession.getStatus().equals(AuctionSessionStatus.INITIALIZED)
                || auctionSession.getStatus().equals(AuctionSessionStatus.STOP)
                || auctionSession.getStatus().equals(AuctionSessionStatus.BIDDING)) {
            auctionSession.setStaffSession(authenticationRepository.findById(auctionSessionRequest.getStaff_id()));
            auctionSession.setStart_time(auctionSessionRequest.getStart_time());
            auctionSession.setEnd_time(auctionSessionRequest.getEnd_time());
            auctionSession.setMinStepPrice(auctionSessionRequest.getMin_stepPrice());
            auctionSession.setDepositAmount(auctionSessionRequest.getDeposit_amount());
            auctionSession.setNameSession(auctionSessionRequest.getName_session());
            auctionSession.setNameJewelry(auctionSessionRequest.getName_jewelry());
            auctionSession.setDescription(auctionSessionRequest.getDescription());
            if (auctionSessionRequest.getResourceSession() != null) {
                resourceRepository.deleteByAuctionSessionResourceId(auctionSession.getId());
                for (ResourceRequest resourceSession : auctionSessionRequest.getResourceSession()) {
                    Resources resources = new Resources();
                    resources.setResourceType(ResourceTypes.ResourceType.img);
                    resources.setPath(resourceSession.getPath());
                    resources.setDescription(resourceSession.getDescription());
                    resources.setReferenceType(ResourceTypes.ReferenceType.AUCTION_SESSION);
                    resources.setAuctionSessionResource(auctionSession);
                    resources.setAccountResource(accountUtils.getAccountCurrent());
                    resources.setUploadAt(new Date());
                    resourceRepository.save(resources);
                }
            }
            // auctionSession.setFeeAmount(auctionSessionRequest.getFee_amount());
            // auctionSession.setCreateAt(new Date());
            // List<Resources> resources =
            // resourceRepository.findByAuctionSessionId(auctionSession.getId());
            auctionSession.setStatus(auctionSessionRequest.getStatus());
//            Date now = new Date();
//            Date start = auctionSession.getStart_time();
//            Date end = auctionSession.getEnd_time();
//
//            if (now.after(start) && (now.before(end))) {
//                auctionSession.setStatus(AuctionSessionStatus.BIDDING);
//            } else if (now.after(end)) {
//                auctionSession.setStatus(AuctionSessionStatus.FINISH);
//            } else if (now.before(start)) {
//                auctionSession.setStatus(AuctionSessionStatus.INITIALIZED);
//            }

            List<AuctionRegistration> allRegister = auctionRegistrationRepository
                    .findAuctionRegistrationByAuctionSessionId(id);
            for (AuctionRegistration registration : allRegister) {
                Account account = registration.getAccountRegistration();
                EmailDetail emailDetail = new EmailDetail();
                emailDetail.setRecipient(account.getEmail());
                emailDetail.setSubject("Update Auction Session Notification");
                emailDetail.setAuctionId(auctionSession.getId());
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
    public void checkTimeSessionStart() {
        List<AuctionSession> startSesion = auctionSessionRepository.findByStatus(AuctionSessionStatus.INITIALIZED);
        List<AuctionSession> endSesion = auctionSessionRepository.findByStatus(AuctionSessionStatus.BIDDING);
        Date now = new Date();
        for (AuctionSession s : startSesion) {
            if (now.equals(s.getStart_time()) || now.after(s.getStart_time())) {
                executorService.submit(() -> {
                    s.setStatus(AuctionSessionStatus.BIDDING);
                    auctionSessionRepository.save(s);
                });
            }
        }
        for (AuctionSession s : endSesion) {
            if (now.after(s.getEnd_time())) {
                executorService.submit(() -> {
                    s.setStatus(AuctionSessionStatus.PENDINGPAYMENT);
                    auctionSessionRepository.save(s);
                        this.finishSession(s.getId());
                });
            }
        }
    }

    public AuctionSession stopAuctionSession(Long id) {
        AuctionSession auctionSession = auctionSessionRepository.findAuctionSessionById(id);
        if(auctionSession.getStaffSession().getId() != accountUtils.getAccountCurrent().getId()&& !accountUtils.getAccountCurrent().getRole().equals(AccountRole.ADMIN)){
            throw new IllegalStateException("You can not assigned edit this session");
        }
        if (auctionSession.getStatus().equals(AuctionSessionStatus.BIDDING)) {
            auctionSession.setStatus(AuctionSessionStatus.STOP);
            auctionSessionRepository.save(auctionSession);
        }
        else{
            throw new IllegalStateException("This session is not BIDDING");
        }
        return auctionSession;
    }
    public AuctionSession continueAuctionSession(Long id) {

        AuctionSession auctionSession = auctionSessionRepository.findAuctionSessionById(id);
        if(auctionSession.getStaffSession().getId() != accountUtils.getAccountCurrent().getId()&& !accountUtils.getAccountCurrent().getRole().equals(AccountRole.ADMIN)){
            throw new IllegalStateException("You can not assigned edit this session");
        }
        if (auctionSession.getStatus().equals(AuctionSessionStatus.STOP)) {
            auctionSession.setStatus(AuctionSessionStatus.BIDDING);
            auctionSessionRepository.save(auctionSession);
        }else{
            throw new IllegalStateException("This session is not STOP");
        }
        return auctionSession;
    }

    // public void deleteSession(Long id) {
    // auctionSessionRepository.deleteById(id);
    // }
    @Transactional
    public void finishSession(long sessionId) {
        AuctionSession auctionSession = auctionSessionRepository.findAuctionSessionById(sessionId);
//        auctionSession.setStatus(AuctionSessionStatus.PENDINGPAYMENT);
//        auctionSessionRepository.save(auctionSession);
        AuctionBid auctionBidHighest = auctionBidRepository.findHighestBidBySessionId(sessionId)
                .orElse(null);

//        Thông báo người dùng đến nhận hàng
        if (auctionBidHighest == null) {
            auctionSession.setStatus(AuctionSessionStatus.EXPIRED);
            auctionSessionRepository.save(auctionSession);
            return;
        } else {
            auctionBidHighest.setStatus(AuctionBidStatus.WON);
            auctionBidHighest.getAuctionRegistration().setStatus(AuctionRegistrationStatus.WON);
            auctionBidRepository.save(auctionBidHighest);
        }
        List<AuctionBid> activeBids = auctionBidRepository.findActiveBidsBySessionId(sessionId);
        for (AuctionBid bid : activeBids) {
            walletService.changBalance(bid.getAuctionRegistration().getAccountRegistration().getWallet().getId(), bid.getBid_price(), TransactionType.REFUND, "RefundBidding amount" + bid.getBid_price());
            bid.setStatus(AuctionBidStatus.REFUND);
            bid.getAuctionRegistration().setStatus(AuctionRegistrationStatus.REFUNDED);
            auctionBidRepository.save(bid);
        }
        walletService.changBalance(auctionSession.getAuctionRequest().getAccountRequest().getWallet().getId(),
                auctionBidHighest.getBid_price(), TransactionType.SELLING, "Sell successfully product with id request"
                        + auctionSession.getAuctionRequest().getId() + "With Price" + auctionBidHighest.getBid_price());
//gửi mail cho thằng chiến thắng nữa
        Account account = auctionBidHighest.getAuctionRegistration().getAccountRegistration();
        EmailDetail emailDetail = new EmailDetail();
            emailDetail.setRecipient(account.getEmail());
            emailDetail.setSubject("Congratulations! You Are the Winning Bidder");
            emailDetail.setFullName(account.getFullname());
            emailDetail.setAuctionId(auctionBidHighest.getAuctionRegistration().getAuctionSession().getId());
            emailDetail.setProductName(auctionBidHighest.getAuctionRegistration().getAuctionSession().getNameJewelry());
        emailService.sendMailNotification(emailDetail, "templateWinner");
        auctionSession.setStatus(AuctionSessionStatus.FINISH);

    }

    public List<AuctionSession> getAuctionSession3days() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, -3);
        Date threeDaysAgo = cal.getTime();
        return auctionSessionRepository.findAuctionSessions3days(AuctionSessionStatus.PENDINGPAYMENT, threeDaysAgo, now);
    }
}
