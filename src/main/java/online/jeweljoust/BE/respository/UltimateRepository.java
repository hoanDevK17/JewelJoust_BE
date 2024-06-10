package online.jeweljoust.BE.respository;

import online.jeweljoust.BE.entity.InitialValuation;
import online.jeweljoust.BE.entity.UltimateValuation;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UltimateRepository extends JpaRepository<UltimateValuation, Long> {
    UltimateValuation findById(long id);

}
