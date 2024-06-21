package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.*;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import online.jeweljoust.BE.enums.InitialValuationsStatus;
import online.jeweljoust.BE.enums.ShipmentStatus;
import online.jeweljoust.BE.enums.UltimateValuationsStatus;
import online.jeweljoust.BE.exception.InvalidStatusException;
import online.jeweljoust.BE.model.ConfirmedInititalRequest;
import online.jeweljoust.BE.model.RejectUltimateRequest;
import online.jeweljoust.BE.model.RejectedInititalPriceRequest;
import online.jeweljoust.BE.model.UltimateRequest;
import online.jeweljoust.BE.respository.AuctionRequestRepository;
import online.jeweljoust.BE.respository.InitialRepository;
import online.jeweljoust.BE.respository.ShipmentRepository;
import online.jeweljoust.BE.respository.UltimateRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
@Service

public class ValuationService {

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    InitialRepository initialRepository;

    @Autowired
    AuctionRequestRepository auctionRepository;

    @Autowired
    ShipmentRepository shipmentRepository;

    @Autowired
    UltimateRepository ultimateRepository;

    public AuctionRequest deliveryStatusById(long id) {
        InitialValuation initialValuation =   auctionRepository.findAuctionRequestById(id).getInitialValuations();
        AuctionRequest auctionRequest = initialValuation.getAuctionRequestInitial();
        Shipment shipment = new Shipment();
        if (initialValuation.getStatus().equals(InitialValuationsStatus.CONFIRMED)){
            LocalDateTime now = LocalDateTime.now();
            shipment.setReceivedDate(now);
            shipment.setStatus(ShipmentStatus.RECEIVED);
            shipment.setAccountShipment(accountUtils.getAccountCurrent());
            shipment.setInitialShipment(initialValuation);
            auctionRequest.setStatus(AuctionRequestStatus.RECEIVED);
            shipmentRepository.save(shipment);
        } else {
            throw new InvalidStatusException("Invalid status to proceed!!!");
        }
        return auctionRepository.save(auctionRequest);
    }

    public List<Shipment> getAllReceived(ShipmentStatus status) {
        List<Shipment> shipmentList = shipmentRepository.findByStatus(status);
        return shipmentList;
    }

    public UltimateValuation ultimateValuation( UltimateRequest ultimateRequest) {
        AuctionRequest auctionRequest = auctionRepository.findAuctionRequestById(ultimateRequest.getId_auctionRequest());
        UltimateValuation ultimateValuation = new UltimateValuation();
        if (auctionRequest.getStatus().equals(AuctionRequestStatus.RECEIVED)){
            LocalDateTime now = LocalDateTime.now();
            ultimateValuation.setUltimatedate(new Date());
            ultimateValuation.setStatus(UltimateValuationsStatus.REVIEW);
            ultimateValuation.setPrice(ultimateRequest.getPrice());
            ultimateValuation.setUltimateStaff(accountUtils.getAccountCurrent());
            auctionRequest.setStatus(AuctionRequestStatus.REVIEW);
            ultimateValuation.setAuctionRequestUltimate(auctionRequest);
        } else {
            throw new IllegalStateException("Invalid status to proceed!!!");
        }
        return  ultimateRepository.save(ultimateValuation);
    }

    public UltimateValuation ultimateValuationReject( RejectUltimateRequest rejectUltimateRequest) {
            AuctionRequest auctionRequest = auctionRepository.findAuctionRequestById(rejectUltimateRequest.getId_auctionRequest());
            UltimateValuation ultimateValuation = new UltimateValuation();
            if (auctionRequest.getStatus().equals(AuctionRequestStatus.RECEIVED)){
                LocalDateTime now = LocalDateTime.now();
                ultimateValuation.setUltimatedate(new Date());
                ultimateValuation.setStatus(UltimateValuationsStatus.UNACCEPTED);
                ultimateValuation.setReason(rejectUltimateRequest.getReason());
                ultimateValuation.setUltimateStaff(accountUtils.getAccountCurrent());
                auctionRequest.setStatus(AuctionRequestStatus.UNACCEPTED);

                ultimateValuation.setAuctionRequestUltimate(auctionRequest);


        } else {
            throw new IllegalStateException("Invalid status to proceed!!!");
        }
        return  ultimateRepository.save(ultimateValuation);
    }
    public UltimateValuation approvalManager(long id) {
        AuctionRequest auctionRequest = auctionRepository.findAuctionRequestById(id);
        UltimateValuation ultimateValuation = auctionRequest.getUltimateValuation();
        if (ultimateValuation.getStatus().equals(UltimateValuationsStatus.REVIEW) ){
            ultimateValuation.setStatus(UltimateValuationsStatus.APPROVED);
            ultimateValuation.setApprovaldanagerdate(new Date());
            ultimateValuation.setUltimateManager(accountUtils.getAccountCurrent());
            auctionRequest.setStatus(AuctionRequestStatus.APPROVED);

            ultimateValuation.setAuctionRequestUltimate(auctionRequest);
        } else {
            throw new IllegalStateException("Invalid status to proceed!!!");
        }
        return ultimateRepository.save(ultimateValuation);
    }
    public UltimateValuation unApprovalManager(long id,String reason) {


        AuctionRequest auctionRequest = auctionRepository.findAuctionRequestById(id);
        UltimateValuation ultimateValuation = auctionRequest.getUltimateValuation();
        if (ultimateValuation.getStatus().equals( UltimateValuationsStatus.REVIEW)){
            ultimateValuation.setStatus(UltimateValuationsStatus.UNAPPROVED);
            ultimateValuation.setApprovaldanagerdate(new Date());
            ultimateValuation.setUltimateManager(accountUtils.getAccountCurrent());
            auctionRequest.setStatus(AuctionRequestStatus.UNAPPROVED);
            ultimateValuation.setAuctionRequestUltimate(auctionRequest);
            ultimateRepository.save(ultimateValuation);
        } else {
            throw new IllegalStateException("Invalid status to proceed!!!");
        }
        return ultimateValuation;
    }

    public InitialValuation comfirmedInitial(ConfirmedInititalRequest confirmedInititalRequest) {
        AuctionRequest auctionRequest = auctionRepository.findAuctionRequestById(confirmedInititalRequest.getId());
        InitialValuation initialValuation = new InitialValuation();
        if (auctionRequest.getStatus().equals(AuctionRequestStatus.PENDING)){
            Account account = accountUtils.getAccountCurrent();
            LocalDateTime now = LocalDateTime.now();
            initialValuation.setInitialdate(now);
            initialValuation.setStatus(InitialValuationsStatus.CONFIRMED);
            initialValuation.setPrice(confirmedInititalRequest.getPrice());
            initialValuation.setAuctionRequestInitial(auctionRequest);
            initialValuation.setAccountInitial(account);
            auctionRequest.setStatus(AuctionRequestStatus.CONFIRMED);
            initialRepository.save(initialValuation);
        } else {
            throw new IllegalStateException("Invalid status to proceed!!!");
        }
        return initialValuation;
    }

    public InitialValuation rejectedInitial(RejectedInititalPriceRequest rejectedInititalPriceRequest) {
        AuctionRequest auctionRequest = auctionRepository.findAuctionRequestById(rejectedInititalPriceRequest.getId());
        InitialValuation initialValuation = new InitialValuation();
        if (auctionRequest.getStatus().equals(AuctionRequestStatus.PENDING)){
            Account account = accountUtils.getAccountCurrent();
            LocalDateTime now = LocalDateTime.now();
            initialValuation.setInitialdate(now);
            initialValuation.setStatus(InitialValuationsStatus.REJECTED);
            initialValuation.setReason(rejectedInititalPriceRequest.getReason());
            initialValuation.setAuctionRequestInitial(auctionRequest);
            initialValuation.setAccountInitial(account);
            auctionRequest.setStatus(AuctionRequestStatus.REJECTED);
            initialRepository.save(initialValuation);
        } else {
            throw new IllegalStateException("Invalid status to proceed!!!");
        }
        return initialValuation;
    }

    @Scheduled(cron = "3 * * * * ?")
    public void checkMissingShipment() {
//        List<InitialValuation> lists = initialRepository.findByStatus(InitialValuationsStatus.CONFIRMED);
//        LocalDateTime now = LocalDateTime.now();
//        System.out.println(lists);
//        for (InitialValuation initial : lists) {
//            if (initial.getStatus().equals(InitialValuationsStatus.CONFIRMED)) {
//                Shipment shipment = initial.getShipment();
//                if (shipment != null && !shipment.getStatus().equals(ShipmentStatus.MISSED)) {
//                    LocalDateTime receivedDate = shipment.getReceivedDate();
//                    long daysSinceReceived = ChronoUnit.DAYS.between(receivedDate, now);
//                    System.out.println(daysSinceReceived);
//                    if (daysSinceReceived <= 14) {
//                        shipment.setStatus(ShipmentStatus.MISSED);
//                        shipmentRepository.save(shipment);
//                        System.out.println("Auto updated status for shipment ID: " + shipment.getId());
//                    }
//                }
//            }
//        }
        System.out.println("done");
    }

}
