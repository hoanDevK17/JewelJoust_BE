package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.entity.InitialValuation;
import online.jeweljoust.BE.entity.Shipment;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import online.jeweljoust.BE.model.InitialRequest;
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

    public InitialValuation changeStatusInitial(long id, InitialRequest initialRequest) {
        AuctionRequest auctionRequest = auctionRepository.findById(id);
        InitialValuation initialValuation = new InitialValuation();
        if (auctionRequest.getStatus().equals(AuctionRequestStatus.initialStatus.PENDING)){
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

    public Shipment deliveryStatusById(long id, AuctionRequestStatus.shipmentStatus status) {
        InitialValuation initialValuation = initialRepository.findById(id);
        Shipment shipment = new Shipment();
        if (initialValuation.getStatus().equals(AuctionRequestStatus.initialStatus.CONFIRMED)){
            LocalDateTime now = LocalDateTime.now();
            shipment.setReceiveddate(now);
            shipment.setStatus(status);
            shipment.setAccountShipment(accountUtils.getAccountCurrent());
            shipment.setInitialShipment(initialValuation);
            shipmentRepository.save(shipment);
        } else {
            throw new IllegalStateException("Status not match!!!");
        }
        return shipment;
    }

    public List<Shipment> getAllReceived(AuctionRequestStatus.shipmentStatus status) {
        List<Shipment> shipmentList = shipmentRepository.findByStatus(status);
        return shipmentList;
    }
}
