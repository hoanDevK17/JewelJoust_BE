package online.jeweljoust.BE.respository;

import online.jeweljoust.BE.entity.Account;
import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.entity.InitialValuation;
import online.jeweljoust.BE.entity.Shipment;
import online.jeweljoust.BE.enums.AuctionRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AuctionRequestRepository extends JpaRepository<AuctionRequest, Long>
{
     AuctionRequest findAuctionRequestById(long id);
     List<AuctionRequest> findByAccountRequestId(long userid);
     List<AuctionRequest> findByStatus(AuctionRequestStatus status);
     @Query("SELECT ar FROM AuctionRequest ar LEFT JOIN ar.auctionSessions asess WHERE ar.status = 'AGREED' AND asess IS NULL")
     List<AuctionRequest> findByAccountRequestAvailable();
     @Query("SELECT ar FROM AuctionRequest ar LEFT JOIN ar.shipment s WHERE ar.status = 'RETURN' AND s IS NULL")
     List<AuctionRequest> findByAccountRequestShipment();
     @Query("SELECT a FROM AuctionRequest a")
     Page<AuctionRequest> findAllAuctionRequests(Pageable pageable);
     Page<AuctionRequest> findByAccountRequestId(Long id, Pageable pageable);

     @Query("SELECT COUNT(a) FROM AuctionRequest a")
     long countTotalAuctionRequests();

     @Query("SELECT MONTH(r.requestdate) AS month, COUNT(r) AS requestCount " +
             "FROM AuctionRequest r " +
             "WHERE YEAR(r.requestdate) = :year " +
             "GROUP BY MONTH(r.requestdate) " +
             "ORDER BY MONTH(r.requestdate)")
     List<Object[]> countRequestsByMonth(@Param("year") long year);

     @Query(value = "SELECT status, COUNT(*) AS auction_requests FROM auction_request GROUP BY status", nativeQuery = true)
     List<Object[]> countAuctionRequestsByStatus();
}
