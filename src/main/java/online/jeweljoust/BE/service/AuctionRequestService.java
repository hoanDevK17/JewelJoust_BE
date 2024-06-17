package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.entity.Resources;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import online.jeweljoust.BE.enums.ResourceTypes;
import online.jeweljoust.BE.model.AuctionRequestReponse;
import online.jeweljoust.BE.model.ResourceRequest;
import online.jeweljoust.BE.respository.AuctionRequestRepository;
import online.jeweljoust.BE.respository.ResourceRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service

public class AuctionRequestService {


    @Autowired
    AuctionRequestRepository auctionRepository;

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    ResourceRepository resourceRepository;

    public AuctionRequest requestSale(AuctionRequestReponse auctionRequestReponse){
        AuctionRequest auctionRequest = new AuctionRequest();
        LocalDateTime now = LocalDateTime.now();

            auctionRequest.setAccountRequest(accountUtils.getAccountCurrent());
            auctionRequest.setRequestdate(now);
            auctionRequest.setJewelryname(auctionRequestReponse.getJewelryName());
            auctionRequest.setJewelrydescription(auctionRequestReponse.getJewelryDescription());
            auctionRequest.setJewelryinitialprice(auctionRequestReponse.getInitialPrice());
            auctionRequest.setStatus(AuctionRequestStatus.PENDING);
            AuctionRequest saveAuctionRequest = auctionRepository.save(auctionRequest);

            for (ResourceRequest resourceRequest : auctionRequestReponse.getResourceRequests()){
                Resources resources = new Resources();
                resources.setResourceType(ResourceTypes.ResourceType.img);
                resources.setPath(resourceRequest.getPath());
                resources.setReferenceType(ResourceTypes.ReferenceType.AUCTION_REQUEST);
                resources.setAuctionRequestResource(saveAuctionRequest);
                resources.setAccountResource(accountUtils.getAccountCurrent());
                resources.setUploadAt(now);
                resourceRepository.save(resources);
            }
        return saveAuctionRequest;
    }

    public List<AuctionRequest> getAuctionRequest() {
        long userid = accountUtils.getAccountCurrent().getId();
        return auctionRepository.findByAccountRequestId(userid);
    }

    public AuctionRequest cancelRequest(long auctionrequestid) {
        AuctionRequest auctionRequest = auctionRepository.findAuctionRequestById(auctionrequestid);
        auctionRequest.setStatus(AuctionRequestStatus.CANCEL);
        return auctionRepository.save(auctionRequest);
    }

    public List<AuctionRequest> getAuctionRequestByStatus(AuctionRequestStatus status) {
        return auctionRepository.findByStatus(status);
    }

    public List<AuctionRequest> getAllAuctionRequest() {
        return auctionRepository.findAll();
    }

//    public List<AuctionRequest> getAllAuctionRequestById() {
//        return auctionRepository.findByAccountRequestId(accountUtils.getAccountCurrent().getId());
//    }
}
