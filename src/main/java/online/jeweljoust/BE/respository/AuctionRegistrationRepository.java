package online.jeweljoust.BE.respository;


import online.jeweljoust.BE.entity.AuctionRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRegistrationRepository extends JpaRepository<AuctionRegistration, Long> {
    AuctionRegistration findAuctionRegistrationById(Long id);
}
