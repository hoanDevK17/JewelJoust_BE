package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.*;
import online.jeweljoust.BE.enums.AuctionRegistrationStatus;
import online.jeweljoust.BE.enums.TransactionType;
import online.jeweljoust.BE.model.AuctionBidRequest;
import online.jeweljoust.BE.model.AuctionRegistrationRequest;
import online.jeweljoust.BE.respository.AuctionBidRepository;
import online.jeweljoust.BE.respository.AuctionRegistrationRepository;
import online.jeweljoust.BE.respository.WalletRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuctionBidService {
    @Autowired
    AuctionRegistrationRepository auctionRegistration;
    @Autowired
    AuctionBidRepository auctionBidRepository;
  WalletService walletService;
  AccountUtils accountUtils;
    public AuctionBid addAuctionBid(AuctionBidRequest auctionBidRequest) {
        Transaction transaction = walletService.changBalance(accountUtils.getAccountCurrent().getWallet().getId(), auctionBidRequest.getPrice(),TransactionType.BIDDING,"Bidding" + auctionBidRequest.getPrice() );

         AuctionBid auctionBid = new AuctionBid();
        auctionBid.setAuctionRegistration(auctionRegistration.findAuctionRegistrationById(auctionBidRequest.getRegistration_id()));
        auctionBid.setBid_price(auctionBidRequest.getPrice());
        auctionBid.setBid_time(new Date());
       if( auctionBidRepository.save(auctionBid)==null){
           throw new IllegalStateException("Some thing went wrong");
       }
       return auctionBid;

//walletService
    }
}
