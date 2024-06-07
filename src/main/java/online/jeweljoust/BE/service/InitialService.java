package online.jeweljoust.BE.service;

import jakarta.persistence.*;
import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.entity.InitialValuation;
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
    InitialRepository initialRepository;

    @Autowired
    AccountUtils accountUtils;

    public static InitialValuation changeStatusInitial(InitialRequest initialRequest) {
        InitialValuation initialValuation = new InitialValuation();
        LocalDateTime now = LocalDateTime.now();
        initialValuation.setInitialdate(now);
        initialValuation.setStatus(initialRequest.getStatus());
        initialValuation.setReason(initialRequest.getReason());
        initialRequest.setPrice(initialRequest.getPrice());
        return initialValuation;
    }

}
