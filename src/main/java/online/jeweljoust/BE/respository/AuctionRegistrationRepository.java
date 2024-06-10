package online.jeweljoust.BE.respository;


import online.jeweljoust.BE.entity.AuctionRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRegistrationRepository extends JpaRepository<AuctionRegistration, Long> {
    AuctionRegistration findAuctionRegistrationById(long id);
}
