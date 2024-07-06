package online.jeweljoust.BE.service;

import online.jeweljoust.BE.entity.*;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import online.jeweljoust.BE.enums.InitialValuationsStatus;
import online.jeweljoust.BE.enums.ShipmentStatus;
import online.jeweljoust.BE.enums.UltimateValuationsStatus;
import online.jeweljoust.BE.model.*;
import online.jeweljoust.BE.respository.AuctionRequestRepository;
import online.jeweljoust.BE.respository.InitialRepository;
import online.jeweljoust.BE.respository.ShipmentRepository;
import online.jeweljoust.BE.respository.UltimateRepository;
import online.jeweljoust.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
    @Autowired
    EmailService emailService;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public AuctionRequest deliveryStatusById(long id) {
        InitialValuation initialValuation = auctionRepository.findAuctionRequestById(id).getInitialValuations();
        AuctionRequest auctionRequest = initialValuation.getAuctionRequestInitial();
        Shipment shipment = new Shipment();
        if (initialValuation.getStatus().equals(InitialValuationsStatus.CONFIRMED)){
            shipment.setReceivedDate(new Date());
            shipment.setStatus(ShipmentStatus.RECEIVED);
            shipment.setAccountShipment(accountUtils.getAccountCurrent());
            shipment.setInitialShipment(initialValuation);
            auctionRequest.setStatus(AuctionRequestStatus.RECEIVED);
            shipmentRepository.save(shipment);
        } else {
            throw new IllegalStateException("Invalid status to proceed!!!");
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
            initialValuation.setInitialdate(new Date());
            initialValuation.setStatus(InitialValuationsStatus.CONFIRMED);
            initialValuation.setPrice(confirmedInititalRequest.getPrice());
            initialValuation.setAuctionRequestInitial(auctionRequest);
            initialValuation.setAccountInitial(account);
            auctionRequest.setStatus(AuctionRequestStatus.CONFIRMED);
            initialRepository.save(initialValuation);
            EmailDetail emailDetail = new EmailDetail();
                emailDetail.setRecipient(auctionRequest.getAccountRequest().getEmail());
                emailDetail.setSubject("Preliminary Appraisal Complete");
                emailDetail.setMsgBody("");
            emailService.sendMailNotification(emailDetail);
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
            initialValuation.setInitialdate(new Date());
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

    @Scheduled(cron = "0 0 0,12 * * ?")
    public void checkMissingShipment() {
        List<InitialValuation> lists = initialRepository.findByStatus(InitialValuationsStatus.CONFIRMED);
        for (InitialValuation iv : lists){
            if (iv.getShipment() == null){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(iv.getInitialdate());
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Date targetDate = calendar.getTime();

                Date now = new Date();
                long delay = (now.getTime() - targetDate.getTime()) / 1000; // delay tính bằng giây

                if (delay > 0) {
                    scheduledExecutorService.schedule(() -> {
                        Shipment shipment = new Shipment();
                        shipment.setStatus(ShipmentStatus.MISSED);
                        AuctionRequest auctionRequest = iv.getAuctionRequestInitial();
                        auctionRequest.setStatus(AuctionRequestStatus.MISSED);
                        shipment.setReceivedDate(new Date()); // hiện tại
                        shipment.setInitialShipment(iv);
                        shipmentRepository.save(shipment);
                        auctionRepository.save(auctionRequest);
                    }, delay, TimeUnit.SECONDS);
                }
            }
        }
    }


}
