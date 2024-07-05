package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.enums.AuctionConfirmStatus;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import online.jeweljoust.BE.enums.UltimateValuationsStatus;
import online.jeweljoust.BE.model.MemberConfirmRequest;
import online.jeweljoust.BE.respository.AuctionConfirmationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service

public class AuctionConfirmationService {

    @Autowired
    AuctionConfirmationRepository confirmationRepository;

    public AuctionRequest confirmByMember(MemberConfirmRequest memberConfirmRequest) {
        AuctionRequest auctionRequest = confirmationRepository.findById(memberConfirmRequest.getId());
        if (auctionRequest.getStatus().equals(AuctionRequestStatus.APPROVED)){
            auctionRequest.setStatus(AuctionRequestStatus.AGREED);
            auctionRequest.setMemberConfirmationDate(new Date());
            confirmationRepository.save(auctionRequest);
        } else {
            throw new IllegalStateException("Invalid status to proceed!!!");
        }
        return auctionRequest;
    }

    public AuctionRequest rejectByMember(MemberConfirmRequest memberConfirmRequest) {
        AuctionRequest auctionRequest = confirmationRepository.findById(memberConfirmRequest.getId());
        if (auctionRequest.getStatus().equals(AuctionRequestStatus.APPROVED)){
            auctionRequest.setStatus(AuctionRequestStatus.DECLINED);
            auctionRequest.setMemberConfirmationDate(new Date());
            confirmationRepository.save(auctionRequest);
        } else {
            throw new IllegalStateException("Invalid status to proceed!!!");
        }
        return auctionRequest;
    }
}
