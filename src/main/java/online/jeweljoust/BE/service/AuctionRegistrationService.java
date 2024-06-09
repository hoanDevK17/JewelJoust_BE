package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.AuctionRegistration;
import online.jeweljoust.BE.entity.Wallet;
import online.jeweljoust.BE.enums.AuctionRegistrationStatus;
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


    public AuctionRegistration addAuctionRegistration (AuctionRegistrationRequest auctionRegistrationRequest){
        AuctionRegistration auctionRegistration = new AuctionRegistration();
        auctionRegistration.setCreate_at(new Date());
        auctionRegistration.setStatus(AuctionRegistrationStatus.PENDING);
        auctionRegistration.setAuctionSession(auctionSessionRepository.findAuctionSessionById(auctionRegistrationRequest.getAuctionSession_id()));
        auctionRegistration.setAccountRegistration(authenticationRepository.findById(auctionRegistrationRequest.getMember_id()));
        return auctionRegistrationRepository.save(auctionRegistration);
    }
    public List<AuctionRegistration> findAllAuctionRegistration (){
        return auctionRegistrationRepository.findAll();
    }
    public AuctionRegistration cancelAuctionRegistration (Long id){
        AuctionRegistration auctionRegistration = auctionRegistrationRepository.findAuctionRegistrationById(id);
        auctionRegistration.setStatus(AuctionRegistrationStatus.CANCELLED);
        return auctionRegistrationRepository.save(auctionRegistration);
    }
    public AuctionRegistration depositAuctionRegistration (Long id){
        AuctionRegistration auctionRegistration = auctionRegistrationRepository.findAuctionRegistrationById(id);
        Double amountDeposit = auctionRegistration.getAuctionSession().getDepositAmount();
        Wallet wallet = auctionRegistration.getAccountRegistration().getWallet();
        if(wallet.getBalance()>amountDeposit){
//            tạo 1 giao dịch ở đây
            auctionRegistration.setStatus(AuctionRegistrationStatus.DEPOSITED);
//            trừ tiền nữa
            wallet.setBalance(wallet.getBalance()-amountDeposit);
            walletRepository.save(wallet);
        }

        return auctionRegistrationRepository.save(auctionRegistration);
    }

}
