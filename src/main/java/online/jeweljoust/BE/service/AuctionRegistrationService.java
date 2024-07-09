package online.jeweljoust.BE.service;

import jakarta.transaction.Transactional;
import online.jeweljoust.BE.entity.*;
import online.jeweljoust.BE.enums.*;
import online.jeweljoust.BE.model.AuctionRegistrationRequest;
import online.jeweljoust.BE.respository.*;

import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Transactional
    public AuctionRegistration addAuctionRegistration(AuctionRegistrationRequest auctionRegistrationRequest) {
        AuctionSession auctionSession = auctionSessionRepository.findAuctionSessionById(auctionRegistrationRequest.getAuctionSession_id());
        if (auctionRegistrationRepository.existsByAccountIdAndSessionId(accountUtils.getAccountCurrent().getId(), auctionSession.getId())) {
            throw new IllegalStateException("You are already registered for this auction session");

        }
        double price =auctionRegistrationRequest.getPrice();
        if(price<auctionSession.getAuctionRequest().getUltimateValuation().getPrice()){
            throw new IllegalStateException("Bid amount must higher start price");
        }
        double balance =  accountUtils.getAccountCurrent().getWallet().getBalance();
        if (balance < price) {
            throw new IllegalStateException("You do not have enough funds in your wallet.");
        }
        AuctionRegistration auctionRegistration = new AuctionRegistration();
        auctionRegistration.setCreate_at(new Date());
        auctionRegistration.setStatus(AuctionRegistrationStatus.PENDING);
        auctionRegistration.setAuctionSession(auctionSessionRepository.findAuctionSessionById(auctionRegistrationRequest.getAuctionSession_id()));
        auctionRegistration.setAccountRegistration((accountUtils.getAccountCurrent()));
        Set<AuctionBid> auctionBids = new HashSet<AuctionBid>();
        auctionBids.add(auctionBidService.handleNewBidTransaction(accountUtils.getAccountCurrent().getWallet().getId(),price,auctionRegistration.getId()));
        auctionRegistration.setAuctionBids(auctionBids);
//         walletService.changBalance(accountUtils.getAccountCurrent().getWallet().getId(),-price, TransactionType.BIDDING,
//                "Deposit session" + auctionSession.getNameSession(),auctionRegistrationRequest.getAuctionSession_id());

        auctionRegistration.setStatus(AuctionRegistrationStatus.INITIALIZED);
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
