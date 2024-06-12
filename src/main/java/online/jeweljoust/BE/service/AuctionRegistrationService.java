package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.AuctionRegistration;
import online.jeweljoust.BE.entity.Transaction;
import online.jeweljoust.BE.entity.Wallet;
import online.jeweljoust.BE.enums.AuctionRegistrationStatus;
import online.jeweljoust.BE.enums.TransactionStatus;
import online.jeweljoust.BE.model.AuctionRegistrationRequest;
import online.jeweljoust.BE.respository.AuctionRegistrationRepository;

import online.jeweljoust.BE.respository.AuctionSessionRepository;
import online.jeweljoust.BE.respository.AuthenticationRepository;
import online.jeweljoust.BE.respository.WalletRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    public AuctionRegistration addAuctionRegistration (AuctionRegistrationRequest auctionRegistrationRequest){

        AuctionRegistration auctionRegistration = new AuctionRegistration();
        auctionRegistration.setCreate_at(new Date());
        auctionRegistration.setStatus(AuctionRegistrationStatus.PENDING);
        auctionRegistration.setAuctionSession(auctionSessionRepository.findAuctionSessionById(auctionRegistrationRequest.getAuctionSession_id()));
        auctionRegistration.setAccountRegistration((accountUtils.getAccountCurrent()));
        return auctionRegistrationRepository.save(auctionRegistration);
    }
    public List<AuctionRegistration> findAllAuctionRegistration (){
        return auctionRegistrationRepository.findAll();
    }
    public AuctionRegistration cancelAuctionRegistration (Long id){
        AuctionRegistration auctionRegistration = auctionRegistrationRepository.findAuctionRegistrationById(id);
        if(auctionRegistration.getStatus()!=AuctionRegistrationStatus.CANCELLED) {
            if(auctionRegistration.getStatus()!=AuctionRegistrationStatus.PENDING  ){

                Transaction transaction = transactionService.refundRegistration(auctionRegistration);

            }
            auctionRegistration.setStatus(AuctionRegistrationStatus.CANCELLED);
            return auctionRegistrationRepository.save(auctionRegistration);
        }
        else{
            throw new IllegalStateException ("this registration had been canceled");
        }
    }
    public AuctionRegistration depositAuctionRegistration (long id){
        AuctionRegistration auctionRegistration = auctionRegistrationRepository.findAuctionRegistrationById(id);
        if(auctionRegistration.getStatus()==AuctionRegistrationStatus.PENDING){
            Transaction transaction = transactionService.deposit(auctionRegistration);
            if (transaction.getStatus()== TransactionStatus.SUCCESSFUL) {
                auctionRegistration.setStatus(AuctionRegistrationStatus.DEPOSITED);
            }
        }
        return auctionRegistrationRepository.save(auctionRegistration);
    }

}
