package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.entity.InitialValuation;
import online.jeweljoust.BE.entity.Shipment;
import online.jeweljoust.BE.entity.UltimateValuation;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import online.jeweljoust.BE.model.ConfirmedInititalRequest;
import online.jeweljoust.BE.model.RejectUltimateRequest;
import online.jeweljoust.BE.model.RejectedInititalPriceRequest;
import online.jeweljoust.BE.model.UltimateRequest;
import online.jeweljoust.BE.service.ValuationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")

public class ValuationAPI {

    @Autowired
    ValuationService valuationService;

//    @PostMapping("/change-status-initial-by-id/{id}")
////    @PreAuthorize("hasAuthority('STAFF')")
//    public ResponseEntity<InitialValuation> changeStatusInitialById(@PathVariable("id") long id, InitialRequest initialRequest) {
//        InitialValuation initialValuation = valuationService.changeStatusInitial(id, initialRequest);
//        return ResponseEntity.ok(initialValuation);
//    }

    @PostMapping("/initialValuations/comfirmed")
//    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<InitialValuation> confirmedInitial(@RequestBody ConfirmedInititalRequest confirmedInititalRequest) {
        InitialValuation initialValuation = valuationService.comfirmedInitial(confirmedInititalRequest);
        return ResponseEntity.ok(initialValuation);
    }

    @PostMapping("/initialValuations/rejected")
//    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<InitialValuation> rejectedInitial(@RequestBody RejectedInititalPriceRequest rejectedInititalPriceRequest) {
        InitialValuation initialValuation = valuationService.rejectedInitial(rejectedInititalPriceRequest);
        return ResponseEntity.ok(initialValuation);
    }

    @PostMapping("/shipments/{id}")
//    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<AuctionRequest> deliveryStatusById(@PathVariable("id") long id) {
        AuctionRequest auctionRequest = valuationService.deliveryStatusById(id);
        return ResponseEntity.ok(auctionRequest);
    }

//        @GetMapping("/shipment/{status}")
////    @PreAuthorize("hasAuthority('MANAGER')")
//    public ResponseEntity<List<Shipment>> getAllReceived(@PathVariable("status") AuctionRequestStatus status) {
//        List<Shipment> shipmentList = valuationService.getAllReceived(status);
//        return ResponseEntity.ok(shipmentList);


    @PostMapping("/ultimateValuations")
//    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<UltimateValuation> ultimateValuation(@RequestBody UltimateRequest ultimateRequest) {
        UltimateValuation ultimateValuation = valuationService.ultimateValuation( ultimateRequest);
        return ResponseEntity.ok(ultimateValuation);
    }
    @PostMapping("/ultimateValuations/rejected")
//    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<UltimateValuation> ultimateValuationReject(@RequestBody RejectUltimateRequest rejectUltimateRequest) {
        UltimateValuation ultimateValuation = valuationService.ultimateValuationReject( rejectUltimateRequest);
        return ResponseEntity.ok(ultimateValuation);
    }

    @PutMapping("/ultimateValuations/{id}")
//    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<UltimateValuation> approvalManager(@PathVariable("id") long id) {
        UltimateValuation ultimateValuation = valuationService.approvalManager(id);
        return ResponseEntity.ok(ultimateValuation);
    }
    @PutMapping("/ultimateValuations/{id}/rejected")
//    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<UltimateValuation> unApprovalManager(@PathVariable("id") long id,@RequestBody String reason) {
        UltimateValuation ultimateValuation = valuationService.unApprovalManager(id,reason);
        return ResponseEntity.ok(ultimateValuation);
    }

}
