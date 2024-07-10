package online.jeweljoust.BE.service;

import jakarta.transaction.Transactional;
import online.jeweljoust.BE.entity.*;
import online.jeweljoust.BE.enums.AuctionBidStatus;
import online.jeweljoust.BE.enums.AuctionRegistrationStatus;
import online.jeweljoust.BE.enums.AuctionSessionStatus;
import online.jeweljoust.BE.enums.TransactionType;
import online.jeweljoust.BE.model.AuctionBidRequest;
import online.jeweljoust.BE.model.AuctionRegistrationRequest;
import online.jeweljoust.BE.respository.AuctionBidRepository;
import online.jeweljoust.BE.respository.AuctionRegistrationRepository;
import online.jeweljoust.BE.respository.AuctionSessionRepository;
import online.jeweljoust.BE.respository.WalletRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

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
    @Transactional
    public AuctionBid addAuctionBid(AuctionBidRequest auctionBidRequest) {


        AuctionSession auctionSession = auctionSessionRepository.findAuctionSessionById(auctionBidRequest.getId_session());
        if(!auctionSession.getStatus().equals(AuctionSessionStatus.BIDDING)){
            throw new
                    IllegalStateException("Cannot bid for this session. The session is not in the bidding phase.");
        }

        AuctionBid highestBid = auctionBidRepository.findHighestBidBySessionId(auctionSession.getId()).orElse(new AuctionBid());

        if(auctionBidRequest.getPrice() < highestBid.getBid_price() ) {
            throw new IllegalArgumentException("Bid amount must be higher than the current highest bid");
        }
        AuctionBid highestBidOfUser = auctionBidRepository.findHighestBidByUserAndSessionAndStatus(accountUtils.getAccountCurrent().getId(),auctionSession.getId()).orElse(new AuctionBid());



        double price = auctionBidRequest.getPrice() - highestBidOfUser.getBid_price();
        AuctionBid auctionBid = this.handleNewBidTransaction(accountUtils.getAccountCurrent().getWallet().getId(),price,auctionBidRequest.getId_session());
        highestBidOfUser.setStatus(AuctionBidStatus.NONACTIVE);
//        auctionBidRepository.save(highestBidOfUser);
//        return auctionBidRepository.save(auctionBid);

        return auctionBid;
//walletService
    }
    @Transactional
    public AuctionBid handleNewBidTransaction(long wallet_id, double price,long registration_id){
        walletService.changBalance(wallet_id, -price,TransactionType.BIDDING,"Bidding" + price );
        AuctionBid auctionBid = new AuctionBid();
        auctionBid.setAuctionRegistration(auctionRegistrationRepository.findAuctionRegistrationById(registration_id));
        auctionBid.setBid_price(price);
        auctionBid.setBid_time(new Date());
        auctionBid.setStatus(AuctionBidStatus.ACTIVE);
        return auctionBid;
    }
}
