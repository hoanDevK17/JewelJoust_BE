package online.jeweljoust.BE.service;

import jakarta.transaction.Transactional;
import online.jeweljoust.BE.entity.*;
import online.jeweljoust.BE.enums.*;
import online.jeweljoust.BE.model.AuctionBidRequest;
import online.jeweljoust.BE.respository.AuctionBidRepository;
import online.jeweljoust.BE.respository.AuctionRegistrationRepository;
import online.jeweljoust.BE.respository.AuctionSessionRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuctionBidService {
    @Autowired
    AuctionRegistrationRepository auctionRegistration;
    @Autowired
    AuctionBidRepository auctionBidRepository;
    @Autowired
    AuctionSessionRepository auctionSessionRepository;
    @Autowired
    AuctionRegistrationRepository auctionRegistrationRepository;
    @Autowired
    WalletService walletService;
    @Autowired
    AccountUtils accountUtils;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Transactional
    public AuctionBid addAuctionBid(AuctionBidRequest auctionBidRequest) {
        AuctionSession auctionSession = auctionSessionRepository.findAuctionSessionById(auctionBidRequest.getId_session());
        if (!auctionSession.getStatus().equals(AuctionSessionStatus.BIDDING)) {
            throw new IllegalStateException("Cannot bid for this session. The session is not in the bidding phase.");
        }
        AuctionBid highestBid = auctionBidRepository.findHighestBidBySessionId(auctionSession.getId()).orElse(new AuctionBid());
        double min_next_price = highestBid.getBid_price() + auctionSession.getMinStepPrice();
        if (auctionBidRequest.getPrice() < (min_next_price)) {
            throw new IllegalStateException("Bid amount must be higher or equal than the current highest bid +  steps price:" + min_next_price);
        }
        AuctionBid highestBidOfUser = auctionBidRepository.findHighestBidByUserAndSessionAndStatus(accountUtils.getAccountCurrent().getId(), auctionSession.getId()).orElse(new AuctionBid());

//        System.out.println(highestBidOfUser.getAuctionRegistration().getAccountRegistration().getId() +"ooo");
//        System.out.println(accountUtils.getAccountCurrent().getId());
//        if (highestBidOfUser.getAuctionRegistration().getAccountRegistration().getId().equals(accountUtils.getAccountCurrent().getId())){
//            throw new IllegalStateException("You are currently the highest bidder");
//        }


        if (highestBid.getAuctionRegistration().getAccountRegistration().getId().equals(accountUtils.getAccountCurrent().getId())) {
            throw new IllegalStateException("You are currently the highest bidder");
        }

        double price = auctionBidRequest.getPrice() - highestBidOfUser.getBid_price();

        AuctionBid auctionBid = this.handleNewBidTransaction(auctionBidRequest.getPrice(), highestBidOfUser.getAuctionRegistration().getId());
        walletService.changBalance(accountUtils.getAccountCurrent().getWallet().getId(), -price, TransactionType.BIDDING, "Bidding" + price);
        highestBidOfUser.setStatus(AuctionBidStatus.NONACTIVE);
        auctionBidRepository.save(highestBidOfUser);
        AuctionBid newauctionBid = auctionBidRepository.save(auctionBid);
        messagingTemplate.convertAndSend("/topic/JewelJoust", "addBid");
        return newauctionBid;


        //walletService
    }

    @Transactional
    public AuctionBid handleNewBidTransaction(double price, long registration_id) {
        AuctionBid auctionBid = new AuctionBid();
        auctionBid.setAuctionRegistration(auctionRegistrationRepository.findAuctionRegistrationById(registration_id));
        auctionBid.setBid_price(price);
        auctionBid.setBid_time(new Date());
        auctionBid.setStatus(AuctionBidStatus.ACTIVE);
        return auctionBid;
    }

    public List<AuctionBid> getHistoryAuctionBid() {
        if (!accountUtils.getAccountCurrent().getRole().equals(AccountRole.MEMBER)) {
            return auctionBidRepository.findAll();
        }
        return auctionBidRepository.findAllBidsByUserId(accountUtils.getAccountCurrent().getId());
    }

    public List<AuctionBid> getListCurrentBidsBySessionId(long id) {

        return auctionBidRepository.findAllBidsBySessionId(id);
    }

    public List<AuctionBid> getBidHistoryByAccountRegistrationId(Long registrationId) {
        return auctionBidRepository.findBidsByAuctionRegistrationId(registrationId);
    }
}
