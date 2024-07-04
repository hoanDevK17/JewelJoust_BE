package online.jeweljoust.BE.respository;


import online.jeweljoust.BE.entity.AuctionRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRegistrationRepository extends JpaRepository<AuctionRegistration, Long> {
    AuctionRegistration findAuctionRegistrationById(long id);
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END " +
            "FROM AuctionRegistration  r " +
            "WHERE r.accountRegistration.Id = :accountId AND r.auctionSession.Id = :sessionId")
    boolean existsByAccountIdAndSessionId(@Param("accountId") Long accountId, @Param("sessionId") Long sessionId);
}
