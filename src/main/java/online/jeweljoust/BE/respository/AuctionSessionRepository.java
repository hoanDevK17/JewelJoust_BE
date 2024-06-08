package online.jeweljoust.BE.respository;

import online.jeweljoust.BE.entity.AuctionSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AuctionSessionRepository extends JpaRepository<AuctionSession, Long> {
    AuctionSession findAuctionSessionById(long id);
    List<AuctionSession> findByNameSession(String name);

}
