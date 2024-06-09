package online.jeweljoust.BE.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.jeweljoust.BE.entity.InitialValuation;
import online.jeweljoust.BE.entity.Shipment;
import online.jeweljoust.BE.entity.UltimateValuation;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import online.jeweljoust.BE.model.InitialRequest;
import online.jeweljoust.BE.model.UltimateRequest;
import online.jeweljoust.BE.service.ValuationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")

public class ValuationAPI {

    @Autowired
    ValuationService valuationService;

    @PutMapping("/change-status-initial-by-id/{id}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<InitialValuation> changeStatusInitialById(@PathVariable("id") long id, InitialRequest initialRequest) {
        InitialValuation initialValuation = valuationService.changeStatusInitial(id, initialRequest);
        return ResponseEntity.ok(initialValuation);
    }

    @PutMapping("/delivery-status-by-id/{id}/{status}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<Shipment> deliveryStatusById(@PathVariable("id") long id, @PathVariable("status") AuctionRequestStatus.shipmentStatus status) {
        Shipment shipment = valuationService.deliveryStatusById(id, status);
        return ResponseEntity.ok(shipment);
    }

    @GetMapping("/get-all-received/{status}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<List<Shipment>> getAllReceived(@PathVariable("status") AuctionRequestStatus.shipmentStatus status) {
        List<Shipment> shipmentList = valuationService.getAllReceived(status);
        return ResponseEntity.ok(shipmentList);
    }

    @PutMapping("/ultimate-valuation/{id}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<UltimateValuation> ultimateValuationById(@PathVariable("id") long id, UltimateRequest ultimateRequest) {
        UltimateValuation ultimateValuation = valuationService.ultimateValuationById(id, ultimateRequest);
        return ResponseEntity.ok(ultimateValuation);
    }
}
