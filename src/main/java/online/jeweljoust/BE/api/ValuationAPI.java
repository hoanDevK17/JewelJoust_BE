package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.InitialValuation;
import online.jeweljoust.BE.entity.Shipment;
import online.jeweljoust.BE.entity.UltimateValuation;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
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

    @PostMapping("/initialValuation/{id}/comfirmed")
//    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<InitialValuation> comfirmedInitial(@PathVariable("id") long id, double price) {
        InitialValuation initialValuation = valuationService.comfirmedInitial(id, price);
        return ResponseEntity.ok(initialValuation);
    }

    @PostMapping("/initialValuation/{id}/rejected")
//    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<InitialValuation> rejectedInitial(@PathVariable("id") long id, String reason) {
        InitialValuation initialValuation = valuationService.rejectedInitial(id, reason);
        return ResponseEntity.ok(initialValuation);
    }

    @PostMapping("/shipment/{id}/{status}")
//    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<Shipment> deliveryStatusById(@PathVariable("id") long id, @PathVariable("status") AuctionRequestStatus status) {
        Shipment shipment = valuationService.deliveryStatusById(id, status);
        return ResponseEntity.ok(shipment);
    }

        @GetMapping("/shipment/{status}")
//    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<List<Shipment>> getAllReceived(@PathVariable("status") AuctionRequestStatus status) {
        List<Shipment> shipmentList = valuationService.getAllReceived(status);
        return ResponseEntity.ok(shipmentList);
    }

    @PostMapping("/ultimateValuation/{id}")
//    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<UltimateValuation> ultimateValuationById(@PathVariable("id") long id, UltimateRequest ultimateRequest) {
        UltimateValuation ultimateValuation = valuationService.ultimateValuationById(id, ultimateRequest);
        return ResponseEntity.ok(ultimateValuation);
    }

    @PutMapping("/ultimateValuation/{id}/{status}/approvalManager")
//    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<UltimateValuation> approvalManager(@PathVariable("id") long id, @PathVariable("status") AuctionRequestStatus status, String reason) {
        UltimateValuation ultimateValuation = valuationService.approvalManager(id, status, reason);
        return ResponseEntity.ok(ultimateValuation);
    }
}
