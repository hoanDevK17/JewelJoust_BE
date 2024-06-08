package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.AuctionSession;

import online.jeweljoust.BE.enums.AuctionSessionStatus;
import online.jeweljoust.BE.model.AuctionSessionRequest;
import online.jeweljoust.BE.respository.AuctionRepository;
import online.jeweljoust.BE.respository.AuctionSessionRepository;
import online.jeweljoust.BE.respository.AuthenticationRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
    AuctionRepository auctionRepository;
    public List<AuctionSession> getAllAuctionSessions(){
        return auctionSessionRepository.findAll();
    }
    public AuctionSession addAuctionSessions(AuctionSessionRequest auctionSessionRequest){

        AuctionSession auctionSession = new AuctionSession();
        auctionSession.setAuctionRequest(auctionRepository.findById(auctionSessionRequest.getAuction_request_id()));
        auctionSession.setManagerSession(accountUtils.getAccountCurrent());
        auctionSession.setStaffSession(authenticationRepository.findById(auctionSessionRequest.getStaff_id()));
        auctionSession.setStart_time(auctionSessionRequest.getStart_time());
        auctionSession.setEnd_time(auctionSessionRequest.getEnd_time());
        auctionSession.setInitialPrice(auctionSessionRequest.getInitial_price());
        auctionSession.setMinStepPrice(auctionSessionRequest.getMin_stepPrice());
        auctionSession.setDepositAmount(auctionSessionRequest.getDeposit_amount());
        auctionSession.setNameSession(auctionSessionRequest.getName_session());
        auctionSession.setNameJewelry(auctionSessionRequest.getName_jewelry());
        auctionSession.setDescription(auctionSessionRequest.getDescription());
        auctionSession.setFeeAmount(auctionSessionRequest.getFee_amount());
        auctionSession.setCreateAt(new Date());
        auctionSession.setStatus(AuctionSessionStatus.INITIALIZED);
//        Initialized: khởi tạo thành công , chưa tới giờ đấu giá
//        Bidding: đang được đấu giá
//        Pending Payment: chờ thanh toán
//        Completed : bán hoàn tất
//        Cancelled : đã hủy

        return auctionSessionRepository.save(auctionSession);
    }
    public AuctionSession updateAuctionSession(long id, AuctionSessionRequest auctionSessionRequest){
        AuctionSession auctionSession =  auctionSessionRepository.findAuctionSessionById(id);
        auctionSession.setAuctionRequest(auctionRepository.findById(auctionSessionRequest.getAuction_request_id()));
        auctionSession.setManagerSession(accountUtils.getAccountCurrent());
        auctionSession.setStaffSession(authenticationRepository.findById(auctionSessionRequest.getStaff_id()));
        auctionSession.setStart_time(auctionSessionRequest.getStart_time());
        auctionSession.setEnd_time(auctionSessionRequest.getEnd_time());
        auctionSession.setInitialPrice(auctionSessionRequest.getInitial_price());
        auctionSession.setMinStepPrice(auctionSessionRequest.getMin_stepPrice());
        auctionSession.setDepositAmount(auctionSessionRequest.getDeposit_amount());
        auctionSession.setNameSession(auctionSessionRequest.getName_session());
        auctionSession.setNameJewelry(auctionSessionRequest.getName_jewelry());
        auctionSession.setDescription(auctionSessionRequest.getDescription());
        auctionSession.setFeeAmount(auctionSessionRequest.getFee_amount());
        auctionSession.setCreateAt(new Date());
        auctionSession.setStatus(AuctionSessionStatus.INITIALIZED);
        return auctionSessionRepository.save(auctionSession);
    }
}

