package online.jeweljoust.BE.respository;

import online.jeweljoust.BE.entity.AuctionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<AuctionRequest, Long>
{
    AuctionRequest findAuctionSaleByUserid (Long userid);
    AuctionRequest findAuctionSaleByAuctionrequestid (Long auctionrequestid);
}
