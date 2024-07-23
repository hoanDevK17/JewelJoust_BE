package online.jeweljoust.BE.service;

import jakarta.transaction.Transactional;
import online.jeweljoust.BE.entity.*;
import online.jeweljoust.BE.enums.*;
import online.jeweljoust.BE.model.AuctionRegistrationRequest;
import online.jeweljoust.BE.respository.*;

import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuctionRegistrationService {
    @Autowired
    AuctionRegistrationRepository auctionRegistrationRepository;

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    AuctionSessionRepository auctionSessionRepository;

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TransactionService transactionService;

    @Autowired
    WalletService walletService;
    @Autowired
    AuctionBidService auctionBidService;
    @Autowired
    AuctionBidRepository auctionBidRepository;
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Transactional
    public AuctionRegistration addAuctionRegistration(AuctionRegistrationRequest auctionRegistrationRequest) {

        AuctionSession auctionSession = auctionSessionRepository.findAuctionSessionById(auctionRegistrationRequest.getAuctionSession_id());
        if(accountUtils.getAccountCurrent().getId().equals(auctionSession.getAuctionRequest().getAccountRequest().getId())){
            throw new IllegalStateException("You can not register this session because you are seller");
        }
        if (auctionRegistrationRepository.existsByAccountIdAndSessionId(accountUtils.getAccountCurrent().getId(), auctionSession.getId()) ) {
            throw new IllegalStateException("You are already registered for this auction session");

        }
        double price =auctionRegistrationRequest.getPrice();
        if(price<auctionSession.getAuctionRequest().getUltimateValuation().getPrice()){
            throw new IllegalStateException("Bid amount must higher start price");
        }
        System.out.println(accountUtils.getAccountCurrent());
        double balance =  accountUtils.getAccountCurrent().getWallet().getBalance();
        if (balance < price) {
            throw new IllegalStateException("You do not have enough funds in your wallet.");
        }
        AuctionRegistration auctionRegistration = new AuctionRegistration();
        auctionRegistration.setCreate_at(new Date());
        auctionRegistration.setStatus(AuctionRegistrationStatus.PENDING);
        auctionRegistration.setAuctionSession(auctionSessionRepository.findAuctionSessionById(auctionRegistrationRequest.getAuctionSession_id()));
        auctionRegistration.setAccountRegistration((accountUtils.getAccountCurrent()));
//        auctionRegistration=
                auctionRegistrationRepository.save(auctionRegistration);
//         walletService.changBalance(accountUtils.getAccountCurrent().getWallet().getId(),-price, TransactionType.BIDDING,
//                "Deposit session" + auctionSession.getNameSession(),auctionRegistrationRequest.getAuctionSession_id());
        AuctionBid auctionBid = auctionBidService.handleNewBidTransaction(price,auctionRegistration.getId());
        walletService.changBalance(accountUtils.getAccountCurrent().getWallet().getId(), -price,TransactionType.BIDDING,"Bidding" + price );
//        auctionBid.setAuctionRegistration(auctionRegistration);
//        Set<AuctionBid> auctionBids = new HashSet<AuctionBid>();
//        auctionBids.add(auctionBidService.handleNewBidTransaction(accountUtils.getAccountCurrent().getWallet().getId(),price,auctionRegistration.getId()));
//        auctionRegistration.setAuctionBids(auctionBids);
        auctionBidRepository.save(auctionBid);
        auctionRegistration.setStatus(AuctionRegistrationStatus.INITIALIZED);

        messagingTemplate.convertAndSend("/topic/JewelJoust","addBid");
        return auctionRegistrationRepository.save(auctionRegistration);
//walletService
    }

    public List<AuctionRegistration> findAllAuctionRegistration() {
        if (accountUtils.getAccountCurrent().getRole().equals(AccountRole.MEMBER)) {
            return auctionRegistrationRepository.findAuctionRegistrationByAccountRegistrationId(accountUtils.getAccountCurrent().getId());
        }
        return (auctionRegistrationRepository.findAll());
    }

    public AuctionRegistration cancelAuctionRegistration(Long id) {
        AuctionRegistration auctionRegistration = auctionRegistrationRepository.findAuctionRegistrationById(id);
        if (auctionRegistration.getStatus() != AuctionRegistrationStatus.CANCELLED) {
            if (auctionRegistration.getStatus() != AuctionRegistrationStatus.PENDING) {
                Transaction transaction = transactionService.refundRegistration(auctionRegistration);

            }
            else {
                throw new IllegalStateException("CAN NOT CANCEL REGISTRATION FOR SESSION IS BIDDING");
            }
            auctionRegistration.setStatus(AuctionRegistrationStatus.CANCELLED);
            return auctionRegistrationRepository.save(auctionRegistration);
        } else {
            throw new IllegalStateException("this registration had been canceled");
        }
    }

//    public AuctionRegistration depositAuctionRegistration(long id) {
//        AuctionRegistration auctionRegistration = auctionRegistrationRepository.findAuctionRegistrationById(id);
//        if (auctionRegistration.getStatus() == AuctionRegistrationStatus.PENDING) {
//            Transaction transaction = transactionService.deposit(auctionRegistration);
//            if (transaction.getStatus() == TransactionStatus.SUCCESSFUL) {
//                auctionRegistration.setStatus(AuctionRegistrationStatus.DEPOSITED);
//            }
//        }
//        return auctionRegistrationRepository.save(auctionRegistration);
//    }

}
