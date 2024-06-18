package online.jeweljoust.BE.respository;

import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.entity.InitialValuation;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import online.jeweljoust.BE.enums.InitialValuationsStatus;
import online.jeweljoust.BE.enums.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface InitialRepository extends JpaRepository<InitialValuation, Long> {
    InitialValuation findById(long id);
    List<InitialValuation> findByStatus(InitialValuationsStatus status);
}
