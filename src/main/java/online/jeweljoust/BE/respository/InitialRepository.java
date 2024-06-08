package online.jeweljoust.BE.respository;

import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.entity.InitialValuation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface InitialRepository extends JpaRepository<InitialValuation, Long> {
//    InitialValuation findByAuctionRequestId(long auctionRequestId);
    AuctionRequest findAuctionRequestById(long id);
}
