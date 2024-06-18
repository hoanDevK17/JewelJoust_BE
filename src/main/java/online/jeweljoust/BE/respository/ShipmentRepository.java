package online.jeweljoust.BE.respository;

import online.jeweljoust.BE.entity.InitialValuation;
import online.jeweljoust.BE.entity.Shipment;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import online.jeweljoust.BE.enums.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    List<Shipment> findByStatus (ShipmentStatus status);
}
