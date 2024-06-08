package online.jeweljoust.BE.service;

import jakarta.persistence.*;
import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.entity.InitialValuation;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import online.jeweljoust.BE.model.InitialRequest;
import online.jeweljoust.BE.respository.AuctionRepository;
import online.jeweljoust.BE.respository.InitialRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class InitialService {

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    InitialRepository initialRepository;

    @Autowired
    AuctionRepository auctionRepository;

    public InitialValuation changeStatusInitial(long id, InitialRequest initialRequest) {
        AuctionRequest auctionRequest = auctionRepository.findById(id);
        InitialValuation initialValuation = new InitialValuation();
        if (auctionRequest.getStatus().equals(AuctionRequestStatus.PENDING)){
            Account account = accountUtils.getAccountCurrent();
            LocalDateTime now = LocalDateTime.now();
            initialValuation.setInitialdate(now);
            initialValuation.setStatus(initialRequest.getStatus());
            initialValuation.setReason(initialRequest.getReason());
            initialValuation.setPrice(initialRequest.getPrice());
            initialValuation.setAuctionRequestInitial(auctionRequest);
            initialValuation.setAccountInitial(account);
            initialRepository.save(initialValuation);
        } else {
            throw new IllegalStateException("Status not match!!!");
        }
    return initialValuation;
    }
}
