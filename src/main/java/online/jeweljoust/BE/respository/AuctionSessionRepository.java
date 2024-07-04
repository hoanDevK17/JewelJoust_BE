package online.jeweljoust.BE.respository;

import online.jeweljoust.BE.entity.AuctionSession;
import online.jeweljoust.BE.enums.AccountStatus;
import online.jeweljoust.BE.enums.AuctionSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AuctionSessionRepository extends JpaRepository<AuctionSession, Long> {
    AuctionSession findAuctionSessionById(long id);
    List<AuctionSession> findByNameSession(String name);
    List<AuctionSession> findByStatus(AuctionSessionStatus status);

}
