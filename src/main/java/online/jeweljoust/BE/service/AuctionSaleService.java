package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.model.AuctionSaleReponse;
import online.jeweljoust.BE.respository.AuctionRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

public class AuctionSaleService {


    @Autowired
    AuctionRepository auctionRepository;

    @Autowired
    AccountUtils accountUtils;

    public AuctionRequest requestSale(AuctionSaleReponse auctionSaleReponse) throws AuthenticationServiceException {
        try {
            String role = accountUtils.getAccountCurrent().getRole();
            if ("Member".equals(role)){
                AuctionRequest auctionRequest = new AuctionRequest();
                LocalDateTime now = LocalDateTime.now();
                auctionRequest.setUserid(accountUtils.getAccountCurrent().getUserid());
                auctionRequest.setRequestdate(now);
                auctionRequest.setJewelryname(auctionRequest.getJewelryname());
                auctionRequest.setJewelrydescription(auctionRequest.getJewelrydescription());
                auctionRequest.setJewelryinitialprice(0);
                auctionRequest.setStatus("Pending");
                return auctionRepository.save(auctionRequest);
            } else {
                throw new AuthenticationServiceException("Your role not accpeted!!!");
            }
        } catch (Exception e){
            e.getMessage();
        }
        return null;
    }

}
