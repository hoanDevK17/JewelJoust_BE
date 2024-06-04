package online.jeweljoust.BE.respository;

import online.jeweljoust.BE.entity.AuctionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<AuctionRequest, Long>
{
    AuctionRequest findAuctionSaleByUserid (Long userid);
    AuctionRequest findAuctionSaleByAuctionrequestid (Long auctionrequestid);
    List<AuctionRequest> findAuctionRequestByUserid(long userid);
    List<AuctionRequest> findAuctionRequestByStatus(String status);
}
