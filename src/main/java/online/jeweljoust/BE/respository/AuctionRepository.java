package online.jeweljoust.BE.respository;

import online.jeweljoust.BE.entity.AuctionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AuctionRepository extends JpaRepository<AuctionRequest, Long>
{

//    AuctionRequest find\\ByUserid (long userid);
//     AuctionRequest findById (long id);
//     List<AuctionRequest> findByAccountId(long userid);

//     AuctionRequest findAuctionSaleByUserid (Long userid);
//     AuctionRequest findAuctionSaleByAuctionrequestid (Long auctionrequestid);
//     List<AuctionRequest> findAuctionRequestByUserid(long userid);
//     List<AuctionRequest> findAuctionRequestByStatus(String status);

}
