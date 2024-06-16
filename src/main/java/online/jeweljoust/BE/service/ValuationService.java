package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.*;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import online.jeweljoust.BE.model.UltimateRequest;
import online.jeweljoust.BE.respository.AuctionRepository;
import online.jeweljoust.BE.respository.InitialRepository;
import online.jeweljoust.BE.respository.ShipmentRepository;
import online.jeweljoust.BE.respository.UltimateRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ValuationService {

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    InitialRepository initialRepository;

    @Autowired
    AuctionRepository auctionRepository;

    @Autowired
    ShipmentRepository shipmentRepository;

    @Autowired
    UltimateRepository ultimateRepository;

    public Shipment deliveryStatusById(long id, AuctionRequestStatus status) {
        InitialValuation initialValuation = initialRepository.findById(id);
        Shipment shipment = new Shipment();
        if (initialValuation.getStatus().equals(AuctionRequestStatus.CONFIRMED)){
            LocalDateTime now = LocalDateTime.now();
            shipment.setReceiveddate(now);
            shipment.setStatus(status);
            shipment.setAccountShipment(accountUtils.getAccountCurrent());
            shipment.setInitialShipment(initialValuation);
            AuctionRequest auctionRequest = initialValuation.getAuctionRequestInitial();
            auctionRequest.setStatus(AuctionRequestStatus.CONFIRMED);
            shipmentRepository.save(shipment);
        } else {
            throw new IllegalStateException("Invalid status to proceed!!!");
        }
        return shipment;
    }

    public List<Shipment> getAllReceived(AuctionRequestStatus status) {
        List<Shipment> shipmentList = shipmentRepository.findByStatus(status);
        return shipmentList;
    }

    public UltimateValuation ultimateValuationById(long id, UltimateRequest ultimateRequest) {
        AuctionRequest auctionRequest = auctionRepository.findById(id);
        UltimateValuation ultimateValuation = new UltimateValuation();
        if (auctionRequest.getInitialValuations() != null){
            AuctionRequestStatus status = auctionRequest.getInitialValuations().getShipment().getStatus();
            if (status.equals(AuctionRequestStatus.RECEIVED)){
                LocalDateTime now = LocalDateTime.now();
                ultimateValuation.setUltimatedate(now);
                ultimateValuation.setApprovaldanagerdate(null);
                ultimateValuation.setStatus(ultimateRequest.getStatus());
                ultimateValuation.setReason(ultimateRequest.getReason());
                ultimateValuation.setPrice(ultimateRequest.getPrice());
                ultimateValuation.setDescription(ultimateRequest.getDescription());
                ultimateValuation.setUltimateStaff(accountUtils.getAccountCurrent());
                ultimateValuation.setUltimateManager(null);
                ultimateValuation.setAuctionRequestUltimate(auctionRequest);
                ultimateRepository.save(ultimateValuation);
            }
        } else {
            throw new IllegalStateException("Invalid status to proceed!!!");
        }
        return ultimateValuation;
    }

    public UltimateValuation approvalManager(long id, AuctionRequestStatus status, String reason) {
        UltimateValuation ultimateValuation = ultimateRepository.findById(id);
        if (ultimateValuation.getStatus().equals(AuctionRequestStatus.REVIEW)){
            LocalDateTime now = LocalDateTime.now();
            ultimateValuation.setStatus(status);
            ultimateValuation.setReason(reason);
            ultimateValuation.setApprovaldanagerdate(now);
            ultimateValuation.setUltimateManager(accountUtils.getAccountCurrent());
            ultimateRepository.save(ultimateValuation);
        } else {
            throw new IllegalStateException("Invalid status to proceed!!!");
        }
        return ultimateValuation;
    }

    public InitialValuation comfirmedInitial(long id, double price) {
        AuctionRequest auctionRequest = auctionRepository.findById(id);
        InitialValuation initialValuation = new InitialValuation();
        if (auctionRequest.getStatus().equals(AuctionRequestStatus.PENDING)){
            Account account = accountUtils.getAccountCurrent();
            LocalDateTime now = LocalDateTime.now();
            initialValuation.setInitialdate(now);
            initialValuation.setStatus(AuctionRequestStatus.CONFIRMED);
            initialValuation.setPrice(price);
            initialValuation.setAuctionRequestInitial(auctionRequest);
            initialValuation.setAccountInitial(account);
            auctionRequest.setStatus(AuctionRequestStatus.CONFIRMED);
            initialRepository.save(initialValuation);
        } else {
            throw new IllegalStateException("Invalid status to proceed!!!");
        }
        return initialValuation;
    }

    public InitialValuation rejectedInitial(long id, String reason) {
        AuctionRequest auctionRequest = auctionRepository.findById(id);
        InitialValuation initialValuation = new InitialValuation();
        if (auctionRequest.getStatus().equals(AuctionRequestStatus.PENDING)){
            Account account = accountUtils.getAccountCurrent();
            LocalDateTime now = LocalDateTime.now();
            initialValuation.setInitialdate(now);
            initialValuation.setStatus(AuctionRequestStatus.REJECTED);
            initialValuation.setReason(reason);
            initialValuation.setAuctionRequestInitial(auctionRequest);
            initialValuation.setAccountInitial(account);
            auctionRequest.setStatus(AuctionRequestStatus.REJECTED);
            initialRepository.save(initialValuation);
        } else {
            throw new IllegalStateException("Invalid status to proceed!!!");
        }
        return initialValuation;
    }
}
