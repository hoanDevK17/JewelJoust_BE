package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.respository.AuctionRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class AuctionRequestService {


    @Autowired
    AuctionRepository auctionRepository;

    @Autowired
    AccountUtils accountUtils;

//    public AuctionRequest requestSale(AuctionSaleReponse auctionSaleReponse) throws AuthenticationServiceException {
//        try {
//            String role = accountUtils.getAccountCurrent().getRole();
//            if ("Member".equals(role)){
//                AuctionRequest auctionRequest = new AuctionRequest();
//                LocalDateTime now = LocalDateTime.now();
//                auctionRequest.setUserid(accountUtils.getAccountCurrent().getUserid());
//                auctionRequest.setRequestdate(now);
//                auctionRequest.setJewelryname(auctionSaleReponse.getJewelryName());
//                auctionRequest.setJewelrydescription(auctionSaleReponse.getJewelryDescription());
//                auctionRequest.setJewelryinitialprice(0);
//                auctionRequest.setStatus("Pending");
//                return auctionRepository.save(auctionRequest);
//            } else {
//                throw new AuthenticationServiceException("Your role not accpeted!!!");
//            }
//        } catch (Exception e){
//            e.getMessage();
//        }
//        return null;
//    }

    public List<AuctionRequest> getAuctionRequest() {
        long userid = accountUtils.getAccountCurrent().getId();
        return auctionRepository.findByAccountId(userid);
    }

    public AuctionRequest cancelRequest(String status, long auctionrequestid) {
        AuctionRequest auctionRequest = auctionRepository.findById(auctionrequestid);
        auctionRequest.setStatus(status);
        return auctionRepository.save(auctionRequest);
    }

    @PreAuthorize("hasAuthority('STAFF')")
    public List<AuctionRequest> getAuctionRequestByStatus(String status) {
        return auctionRepository.findAuctionRequestByStatus(status);
    }
}
