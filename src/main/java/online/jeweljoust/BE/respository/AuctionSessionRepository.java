package online.jeweljoust.BE.respository;

import online.jeweljoust.BE.entity.AuctionRequest;
import online.jeweljoust.BE.entity.AuctionSession;
import online.jeweljoust.BE.enums.AuctionSessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface AuctionSessionRepository extends JpaRepository<AuctionSession, Long> {
    AuctionSession findAuctionSessionById(long id);
    List<AuctionSession> findByNameSessionContaining(String name);
    List<AuctionSession> findByStatus(AuctionSessionStatus status);
    List<AuctionSession> findAuctionSessionByStatus(AuctionSessionStatus status);
    @Query("SELECT ar.auctionSession FROM AuctionRegistration ar WHERE ar.accountRegistration.id = :userId")
    List<AuctionSession> findAuctionSessionRegisteredByUserId(long userId);
    @Query("SELECT a FROM AuctionSession a WHERE a.status = :status AND a.start_time >= :startTime AND a.start_time < :endTime ORDER BY a.start_time DESC")
    List<AuctionSession> findAuctionSessions3days(@Param("status") AuctionSessionStatus status, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
    @Query("SELECT a FROM AuctionSession a")
    Page<AuctionSession> findAllAuctionSessions(Pageable pageable);
}
