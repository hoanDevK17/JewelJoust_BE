package online.jeweljoust.BE.respository;

import online.jeweljoust.BE.entity.AuctionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionConfirmationRepository extends JpaRepository<AuctionRequest, Long> {
    AuctionRequest findById(long id);
}
