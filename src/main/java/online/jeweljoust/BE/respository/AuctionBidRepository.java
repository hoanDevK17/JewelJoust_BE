package online.jeweljoust.BE.respository;

import online.jeweljoust.BE.entity.AuctionBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionBidRepository extends JpaRepository<AuctionBid, Long> {
    AuctionBid findAuctionBidById(long id);

    //    @Query("SELECT b FROM AuctionBid b WHERE b.auctionRegistration.auctionSession_id = :sessionId " +
//            "AND b.amount = (SELECT MAX(b2.amount) FROM AuctionBid b2 WHERE b2.auctionRegistration.auctionSession_id = :sessionId)")
    @Query("SELECT b FROM AuctionBid b WHERE b.auctionRegistration.auctionSession.id = :sessionId " +
            "AND b.bid_price = (SELECT MAX(b2.bid_price) FROM AuctionBid b2 WHERE b2.auctionRegistration.auctionSession.id = :sessionId)")
    Optional<AuctionBid> findHighestBidBySessionId(@Param("sessionId") Long sessionId);
//("SELECT b FROM AuctionBid b " +
//        "WHERE b.bid_price = (SELECT MAX(b2.bid_price) FROM AuctionBid b2 " +
//        "WHERE b2.auctionRegistration.accountRegistration.id = :userId " +
//        "AND b2.auctionRegistration.auctionSession.id = :sessionId " +
//        "AND b2.status = 'ACTIVE')")
    @Query
            (  "SELECT b FROM AuctionBid b " +
                    "WHERE b.auctionRegistration.accountRegistration.id = :userId " +
                    "AND b.auctionRegistration.auctionSession.id = :sessionId " +
                    "AND b.bid_time = (SELECT MAX(b2.bid_time) FROM AuctionBid b2 " +
                    "WHERE b2.auctionRegistration.accountRegistration.id = :userId " +
                    "AND b2.auctionRegistration.auctionSession.id = :sessionId)")
    Optional<AuctionBid> findHighestBidByUserAndSessionAndStatus(@Param("userId") Long userId, @Param("sessionId") Long sessionId);
    @Query("SELECT b FROM AuctionBid b WHERE b.auctionRegistration.auctionSession.id = :sessionId " +
            "AND b.status = 'ACTIVE'")
    List<AuctionBid> findActiveBidsBySessionId(Long sessionId);
    @Query("SELECT b FROM AuctionBid b WHERE b.auctionRegistration.accountRegistration.id = :userId " +
            "AND b.status = 'ACTIVE'")
    List<AuctionBid> findActiveBidsByUserId(Long userId);
//    List<AuctionBid> find
}